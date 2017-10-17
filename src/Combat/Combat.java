/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Combat;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Main.Handler;
import UI.UITextBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Responsible for driving all combat logic in the game
 * @author Soup
 */
public class Combat implements Runnable{
    private Handler handler; //The handler
    private Thread thread; //Combat thread
    private PlayableActor turn; //The PlayableActor whose turn it is currently
    private boolean ready = false; //Whether the combat thread is ready to receive button input or not
    private ArrayList<PlayableActor> party; //The Player's party
    private ArrayList<Enemy> enemyParty; //The enemy party
    private ArrayList<Actor> actors; //All of the actors participating in the battle
    
    public Combat(Handler handler, ArrayList<PlayableActor> party, ArrayList<Enemy> enemyParty){
        this.handler = handler;
        this.party = party;
        this.enemyParty = enemyParty;
        this.actors = new ArrayList<Actor>();
    }
    
    public void init(){
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
        
        //Temporary; displays Actor info
        UITextBox.resetBAOS();
        for (Actor actor : actors){
            System.out.print(actor.getName() + " - ");
            System.out.println("HP: " + actor.getHitpoints() + '/' + actor.getMaxHP());
            System.out.println("MP: " + actor.getMana() + '/' + actor.getMaxMP() + "\n");
        }
        
        //While both parties have at least one living member each, run the combat logic
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
            
            //Iterate through the list of Actors and have each one take their turn
            for (Actor actor : actors)
                actor.takeTurn();
            
            /*
            Commented out for now; can't think of a reason to remove dead Actors from list of Actors (especially since Player still needs to see dead party member info)
            
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
            
            UITextBox.resetBAOS();
            for (Actor actor : actors){
                System.out.print(actor.getName() + " - ");
                System.out.println("HP: " + actor.getHitpoints() + '/' + actor.getMaxHP());
                System.out.println("MP: " + actor.getMana() + '/' + actor.getMaxMP() + "\n");
            }
        }
        
        //If the Player won the battle, distribute each enemy's experience points to the entire party; if the Player lost, display a loss message and reload last saved game
        if (isPartyAlive() && !isEnemyAlive()){
            for (PlayableActor member : party){
                //Party members only gain experience if they're still alive at the end of the battle
                if (member.isAlive()){
                    for (Actor enemy : enemyParty)
                        member.gainExp(enemy.getExp()); //Increase party member's experience by set enemy amount
                }
            }
        }
        else if (!isPartyAlive() && isEnemyAlive()){
            //Load the last saved game
        }
        
        stop(); //Combat is over, stop the thread
    }
    
    /**
     * Determines whether or not the Player's entire party is dead
     * @return True if at least one party member is still alive, or false if they're all dead
     */
    public boolean isPartyAlive(){
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
    public boolean isEnemyAlive(){
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
    
    /**
     * Starts the combat thread
     */
    public synchronized void start(){
        thread = new Thread(this);
        handler.setCombat(thread); //Pass the thread through the handler
        thread.start();
    }
    
    /**
     * Ends the combat thread
     */
    public synchronized void stop(){
        try{
            thread.join();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
}