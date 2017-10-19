/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Combat;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
import Entities.Creatures.Actors.Enemies.Goblin;
import Main.Handler;
import java.util.ArrayList;
import java.util.Random;

/**
 * Encounters determine how many and what type of enemy the Player encounters. There are two types: 
 * Random encounters, which are triggered after the Player takes a certain number of steps, and will
 * usually contain normal run-of-the-mill monsters native to the environment; and set encounters, which
 * are triggered with the Player walks on certain tiles, and are usually reserved for bosses or special
 * fights.
 * @author Soup
 */
public class Encounter {
    private static Random dieRoll = new Random(); //A Random object for determining various outcomes
    
    private Handler handler; //The handler
    private ArrayList<Enemy> enemyParty; //The enemy party
    private int steps; //The number of steps (tiles) the Player must walk until the encounter triggers
    
    public Encounter(Handler handler){
        this.handler = handler;
        this.enemyParty = new ArrayList<Enemy>();
        this.steps = dieRoll.nextInt(26) + 5; //Set the number of steps until the encounter is triggered (anywhere between 5 and 30)
        
        enemyParty.add(new Goblin(handler, enemyParty));
    }
    
    //GETTERS/SETTERS
    
    public ArrayList<Enemy> getParty(){
        return enemyParty;
    }
    
    public int getSteps(){
        return steps;
    }
}
