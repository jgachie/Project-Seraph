/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enums;

import Entities.Creatures.Actors.Actor;
import java.util.Random;

/**
 *
 * @author Soup
 */
public enum StatusEffect {
    //PERSISTENT EFFECTS
    POISON("Poison", new Stat[]{}, new int[]{}){
        @Override
        public void effect(Actor target){
            target.dealDamage(5);
            
            System.out.println(target.getName() + " was hurt by Poison!");
        }
    }, //Damages the Actor for a few hitpoints every turn
    TOXIC("Toxic", new Stat[]{}, new int[]{}){
        @Override
        public void effect(Actor target){
            target.dealDamage(10);
            
            System.out.println(target.getName() + " was hurt by Toxic!");
        }
    }, //More effective version of poison
    STUN("Stun", new Stat[]{}, new int[]{}){
        @Override
        public void effect(Actor target){
        }
    }, //Paralyzes the Actor, making them incapable of taking a turn
    FREEZE("Freeze", new Stat[]{}, new int[]{}){
        @Override
        public void effect(Actor target){
            target.dealDamage(10);
            
            System.out.println(target.getName() + " was hurt by Frostbite!");
        }
    }, //Same thing as stun, but also hurts the Actor for a few hitpoints every turn
    
    //TEMPORARY EFFECTS
    DRAGON_SKIN ("Dragon Skin", new Stat[]{Stat.DEFENSE}, new int[]{3}){
        @Override
        public void effect(Actor target){
        }
    }, //Only applied on Sariel; increases defense by 3 and adds recoil damage to physical attackers
    
    //MISC
    NONE (null, new Stat[]{}, new int[]{}){
        @Override
        public void effect(Actor target){
        }
    }; //Only ever applied to weapons; just means that the weapon doesn't inflict any kind of status effect//Only ever applied to weapons; just means that the weapon doesn't inflict any kind of status effect

    /**
     * An integer constant to be used as the turn value for stats effects that are persistent, and therefore
     * don't expire.
     */
    public static final int PERSISTENT = -1;
    
    private final String value; //The name of the status effect
    private final Stat[] stats; //An array of the stats it modifies
    private final int[] modifiers; //An array of the amount to which each stat in the stats array is modified
    
    //NOTE: Should replace Stat and int arrays with a single HashMap
    
    private StatusEffect(String value, Stat[] stats, int[] modifiers){
        this.value = value;
        this.stats = stats;
        this.modifiers = modifiers;
    }
    
    public abstract void effect(Actor target);
    
    public String getValue(){
        return value;
    }
    
    public Stat[] getStats(){
        return stats;
    }
    
    public int[] getModifiers(){
        return modifiers;
    }
}
