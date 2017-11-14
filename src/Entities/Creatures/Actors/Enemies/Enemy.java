/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Creatures.Actors.Enemies;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.Stat;
import Items.Equipment.Weapon;
import Main.Handler;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public abstract class Enemy extends Actor{
    protected ArrayList<Enemy> enemyParty; //The Enemy's party
    
    protected Enemy(Handler handler, float x, float y, int width, int height, String name, Weapon weapon,
            int level, int hitpoints, int mana, int exp, int strength, int dexterity, int wisdom, int intelligence,
            int luck, int defense, int agility, ArrayList<Enemy> enemyParty){
        super(handler, x, y, width, height, name, weapon, level, hitpoints, mana, exp, strength, dexterity,
                wisdom, intelligence, luck, defense, agility);
        this.enemyParty = enemyParty;
    }
    
    public abstract void decide(ArrayList<PlayableActor> party);
    
    @Override
    public void modifyStat(Stat stat, int modify){
        switch (stat){
            case STRENGTH:
                strength += modify;
                break;
            case DEXTERITY:
                dexterity += modify;
                break;
            case WISDOM:
                wisdom += modify;
                break;
            case INTELLIGENCE:
                intelligence += modify;
                break;
            case LUCK:
                luck += modify;
                break;
            case DEFENSE:
                defense += modify;
                break;
            case AGILITY:
                agility += modify;
                break;
            default:
                //Should never get here; if you do, FUCKING PANIC
                break;
        }
    }

    public ArrayList<Enemy> getParty() {
        return enemyParty;
    }

    public void setParty(ArrayList<Enemy> enemyParty) {
        this.enemyParty = enemyParty;
    }
}