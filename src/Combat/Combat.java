/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Combat;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.StatusEffect;
import Main.Handler;
import UI.UITextBox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Responsible for driving all combat logic in the game
 * @author Soup
 */
public class Combat implements Runnable, Serializable{
    private static final int DELAY = 1500; //The length of the delay between displaying messages and taking turns
    
    private Handler handler; //The handler
    private transient Thread thread; //Combat thread
    private PlayableActor turn; //The PlayableActor whose turn it is currently
    private boolean ready; //Whether the combat thread is ready to receive button input or not
    private int numTurns; //The number of turns that have passed so far
    private ArrayList<PlayableActor> party; //The Player's party
    private ArrayList<Enemy> enemyParty; //The enemy party
    private ArrayList<Actor> actors; //All of the actors participating in the battle
    
    public Combat(Handler handler, ArrayList<PlayableActor> party, ArrayList<Enemy> enemyParty){
        this.handler = handler;
        this.party = party;
        this.enemyParty = enemyParty;
        this.actors = new ArrayList<Actor>();
        this.ready = false;
        this.numTurns = 0;
    }
    
    public void init(){
        //Run through Player's party and save all of their stats to account for combat modifications
        for (PlayableActor member : party)
            member.saveStats();
        
        //Iterate through Player's party and add them to the list of Actors
        for (Actor actor : party)
            actors.add(actor);
        
        //Iterate through enemy party and add them to the list of Actors
        for (Actor actor : enemyParty)
            actors.add(actor);
    }
    
    @Override
    public void run(){
        init(); //Initialize...stuff
        
        actorStatus(); //Display actor status
        
        //While both parties have at least one living member each, run the combat logic
        combatLoop:
        while (isPartyAlive() && isEnemyAlive()){
            //Iterate through Player's party for each member's turn,
            for (PlayableActor member : party){
                turn = member; //Set the current member as active
                try {
                    synchronized (this){
                        ready = true;
                        this.wait(); //Wait for the Player to make a decision
                        ready = false;
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            
            for (Enemy member : enemyParty)
                member.decide(party);
            
            
            //Use a Comparator to sort the Actors in descending order of their agility stats to determine takeTurn order
            Collections.sort(actors, new Comparator<Actor>(){
                @Override
                public int compare(Actor actor1, Actor actor2){
                    if (actor1.getAgility() == actor2.getAgility())
                        return 0;
                    
                    return actor1.getAgility() > actor2.getAgility() ? -1 : 1; //Fancy if-else statement; basically, if actor1 has higher agility than actor2, return -1. Otherwise, return 1
                }
            });
            
            //Iterate through each Actor and, if they're alive, have them take their turn
            for (Actor actor : actors){
                if (actor.isAlive()){
                    ArrayList<StatusEffect> effects = actor.getStatus();
                    
                    //If the Actor is stunned or frozen, skip their turn
                    if (effects.contains(StatusEffect.FREEZE)){
                        UITextBox.resetBAOS();
                        System.out.println(actor.getName() + " is frozen!");
                        delay();
                        continue;
                    }
                    else if (effects.contains(StatusEffect.STUN)){
                        UITextBox.resetBAOS();
                        System.out.println(actor.getName() + " is stunned!");
                        delay();
                        continue;
                    }
                    
                    actor.takeTurn();
                    
                    delay();
                    
                    if (actor.isFleeing())
                        break combatLoop;
                }
            }
            
            //Iterate through each Actor and, if they're alive, update their status effects
            for (Actor actor : actors){
                
                if (actor.isAlive())
                    actor.updateStatus(numTurns);
            }
            
            actorStatus();
            
            numTurns++;
            
            /*
            Commented out for now; can't think of a reason to remove dead Actors from list of Actors (especially since player still needs to see dead party member info)
            
            //Iterate through the Player's party and remove all the dead member from the list of Actors and re-add those that were revived
            for (PlayableActor member : party){
            //If the member is now alive and isn't in the list, add them back; conversely, if the member is dead and is still in the list, remove them
            if (member.isAlive() && !actors.contains(member))
            actors.add(member);
            else if (!member.isAlive() && actors.contains(member))
            actors.remove(member);
            }
            
            //Do the same for the enemy party
            for (Actor member : enemyParty){
            //If the member is now alive and isn't in the list, add them back; conversely, if the member is dead and is still in the list, remove them
            if (member.isAlive() && !actors.contains(member))
            actors.add(member);
            else if (!member.isAlive() && actors.contains(member))
            actors.remove(member);
            }
            
            */
            
        }
        
        UITextBox.resetBAOS(); //Reset the BAOS one last time
        
        //Run through Player's party and reload each member's original, unmodified stats; also, reset each member's fleeing flag to false
        for (PlayableActor member : party){
            member.loadStats();
            member.setFleeing(false);
        }
        
        //If the Player won the battle, distribute each enemy's experience points to the entire party, and save the state of each member
        if (isPartyAlive() && !isEnemyAlive()){
            for (PlayableActor member : party){
                //Party members only gain experience if they're still alive at the end of the battle
                if (member.isAlive()){
                    for (Actor enemy : enemyParty)
                        member.gainExp(enemy.getExp()); //Increase party member's experience by set enemy amount
                }
                
                member.save(); //Save the state of the party member
            }
        }
        //If the Player lost, display a loss message and reload last saved game
        else if (!isPartyAlive() && isEnemyAlive()){
            //Load the last saved game
            
        }
        //If the Player (or the Enemy) fled, simply end combat
        else{
            //Save the state of each party member
            for (PlayableActor member : party)
                member.save();
        }
        
        stop(); //Combat is over, stop the thread
    }
    
    /**
     * Prints out the status of all of the Actors in combat
     */
    private void actorStatus(){
        UITextBox.resetBAOS();
        for (PlayableActor member : party){
            System.out.println(member.getName());
            System.out.println("    HP: " + member.getHitpoints() + '/' + member.getMaxHP());
            System.out.println("    MP: " + member.getMana() + '/' + member.getMaxMP());
            System.out.println("    SP: " + member.getSkillpoints() + '/' + member.getMaxSP());
        }
        
        System.out.print("|"); //Print a pipe to let the textbox know to separate the text into sections
        
        for (Enemy member : enemyParty){
            System.out.println(member.getName());
            System.out.println("    HP: " + member.getHitpoints() + '/' + member.getMaxHP());
            System.out.println("    MP: " + member.getMana() + '/' + member.getMaxMP());
            System.out.println("");
        }
    }
    
    /**
     * Makes the Combat thread wait until animations complete
     */
    public void animationDelay(){
        try {
            synchronized (this){
                this.wait(); //Wait for the animation to complete
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Sleeps the thread for the duration of the combat delay; used so that combat calculations and
     * outcomes aren't displayed all at once
     */
    public static void delay(){
        try{
            Thread.sleep(DELAY);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Sleeps the thread for a given duration; used so that combat calculations and outcomes aren't
     * displayed all at once
     * @param time The amount of time for which the Thread is to sleep
     */
    public static void delay(int time){
        try{
            Thread.sleep(time);
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Determines whether or not the Player's entire party is dead
     * @return True if at least one party member is still alive, or false if they're all dead
     */
    private boolean isPartyAlive(){
        //Iterate through Player's party to see if there are any party members still alive
        for (PlayableActor member : party){
            //If at least one party member is still alive, return true
            if (member.isAlive())
                return true;
        }
        
        return false; //If all party members are dead, return false
    }
    
    /**
     * Determines whether or not the entire enemy party is dead
     * @return True if at least one party member is still alive, or false if they're all dead
     */
    private boolean isEnemyAlive(){
        //Iterate through enemy party to see if there are any party members still alive
        for (Enemy member : enemyParty){
            //If at least one party member is still alive, return true
            if (member.isAlive())
                return true;
        }
        
        return false; //If all party members are dead, return false
    }
    
    //GETTERS/SETTERS
    
    public PlayableActor getTurn(){
        return turn;
    }
    
    public boolean isReady(){
        return ready;
    }
    
    public int getNumTurns() {
        return numTurns;
    }
    
    /**
     * Starts the combat thread
     */
    public synchronized void start(){
        thread = new Thread(this);
        thread.setName("Combat Thread");
        handler.setCombat(this); //Pass the combat object to the handler
        thread.start();
    }
    
    /**
     * Ends the combat thread
     */
    public synchronized void stop(){
        handler.getGame().setState("Game");
        thread = null;
    }
}