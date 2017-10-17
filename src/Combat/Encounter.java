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
    
    public Encounter(Handler handler){
        this.handler = handler;
        enemyParty = new ArrayList<Enemy>();
        
        enemyParty.add(new Goblin(handler));
        
        handler.setEncounter(this);
    }
    
    public ArrayList<Enemy> getParty(){
        return enemyParty;
    }
}
