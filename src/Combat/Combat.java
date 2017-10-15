/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Combat;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActor.PlayableActor;
import Main.Handler;
import java.util.ArrayList;

/**
 * Responsible for driving all combat logic in the game
 * @author Soup
 */
public class Combat implements Runnable{
    private Handler handler; //The handler
    private Thread thread; //Combat thread
    private ArrayList<PlayableActor> party; //The Player's party
    private ArrayList<Actor> enemyParty; //The enemy party
    
    public Combat(Handler handler, ArrayList<PlayableActor> party, ArrayList<Actor> enemyParty){
        this.handler = handler;
        this.party = party;
        this.enemyParty = enemyParty;
    }
    
    private void init(){
        
    }

    @Override
    public void run() {
        init();
        
        /*
        Combat logic goes here
        
        Run Player and enemy decision making separately, and don't care about the order; speed stat
        will determine order later
        
        Player only sees what happens in CombatState. After hitting buttons to decide what to do for
        their turn, those decisions are saved somehow (possibly as UIButton fields in the Actor class)
        and fed through to here.
        
        Once decision-making is over, dump all of the Player's characters and the enemies into one ArrayList
        of Actors. Run a Comparator over this list to compare speed stats and determine turn order. 
        Then, execute each turn in set order.
        */
    }
    
    /**
     * Starts the combat thread
     */
    public synchronized void start(){
        thread = new Thread(this);
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
