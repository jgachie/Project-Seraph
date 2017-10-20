/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Spells;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import UI.UITextBox;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Soup
 */
public abstract class Spell implements Serializable{
    protected static Random dieRoll = new Random(); //A Random object for determining various outcomes
    
    protected final String name; //The name of the spell
    protected final String desc; //The spell's description
    protected final int manaReq; //The amount of mana required to cast the spell
    protected final int baseChance; //The base chance of the spell succeeding
    protected final int critChance; //The base chance of the spell getting a critical hit
    protected final int minDamage, maxDamage; //The lowest and highest amounts of base damage the spell can deal
    protected final boolean friendly; //Whether or not the spell is to be used on the Player's party or the enemy's; true if the Player's party, false if the enemy's
    protected final boolean multi; //Whether the spell is to be used on a single target or multiple targets; true if multiple targets, false if only one
    
    protected Spell(String name, String desc, int manaReq, int baseChance, int critChance, int minDamage,
            int maxDamage, boolean friendly, boolean multi){
        this.name = name;
        this.desc = desc;
        this.manaReq = manaReq;
        this.baseChance = baseChance;
        this.critChance = critChance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.friendly = friendly;
        this.multi = multi;
    }
    
    //For spells that don't deal damage
    protected Spell(String name, String desc, int manaReq, int baseChance, boolean friendly, boolean multi){
        this.name = name;
        this.desc = desc;
        this.manaReq = manaReq;
        this.baseChance = baseChance;
        this.critChance = 0;
        this.minDamage = 0;
        this.maxDamage = 0;
        this.friendly = friendly;
        this.multi = multi;
    }
    
    /**
     * Casts the spell
     * @param caster The Actor casting the spell
     * @param target The Actor being targeted
     */
    public abstract void cast(Actor caster, Actor target);
    
    /**
     * Uses caster's wisdom and spell's base damage to calculate initial damage dealt before running
     * resistances
     * @param caster The Actor casting the spell
     * @return The amount of damage dealt
     */
    protected int calcDamage(Actor caster){
        int wisdom = caster.getWisdom();
        int damage = 0;
        int baseDamage = dieRoll.nextInt(maxDamage + 1) + minDamage; //Roll for base damage using spell's min and max damage as range
        
        //Calculate damage dealt
        if (wisdom < 25)
            damage = (int) (baseDamage * (wisdom / 5.0));
        else if (25 <= wisdom && wisdom < 50)
            damage = (int) (baseDamage * (wisdom / 10.0) + (baseDamage * 2.4));
        else if (wisdom > 50)
            damage = (int) (baseDamage * (wisdom / 20.0) + (baseDamage * 4.9));
        
        //If the spell was critical, multiply the damage by 2
        if (spellCrit(caster)){
            System.out.println("It was a critical hit!");
            damage *= 2;
        }
        
        return damage;
    }
    
    /**
     * Tests intelligence against given base chance of success to determine whether a spell hits or
     * not
     * @param caster The Actor casting the spell
     * @return True if the spell hit; false if it missed
     */
    protected boolean spellHit(Actor caster){
        boolean hit = false; //Whether or not the spell will succeed; initialized to false
        int intelligence = caster.getIntelligence();
        int accuracy = dieRoll.nextInt(100); //Roll for accuracy
        
        //Determine whether the hit was critical
        if (intelligence < 25){
            if (accuracy <= baseChance - intelligence)
                hit = false;
            else
                hit = true;
        }
        else if (25 <= intelligence && intelligence < 50){
            if (accuracy <= baseChance - (baseChance / (baseChance / 12)) - (intelligence / 2))
                hit = false;
            else
                hit = true;
        }
        else if (intelligence >= 50){
            if (accuracy <= baseChance - (baseChance / (baseChance / 12)) - 50)
                hit = false;
            else
                hit = true;
        }
        
        return hit;
    }
    
    /**
     * Tests luck against given critical hit chance to determine whether a spell is critical or not
     * @param caster The actor casting the spell
     * @return True if the spell was critical; false if it wasn't
     */
    protected boolean spellCrit(Actor caster){
        boolean crit = false; //Whether or not the spell will be critical; initialized to false
        int luck = caster.getLuck();
        int chance = dieRoll.nextInt(100); //Roll for crit
        
        if (luck < 25){
            if (chance >= critChance + (luck / 3))
                crit = false;
            else
                crit = true;
        }
        else if (25 <= luck && luck < 50){
            if (chance >= critChance + (luck / 6))
                crit = false;
            else
                crit = true;
        }
        else if (luck >= 50){
            if (chance >= critChance + (luck / 9))
                crit = false;
            else
                crit = true;
        }
        
        //Obviously, this shit is broken, but I can't be bothered to fix it right now
        
        return crit;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public int getManaReq() {
        return manaReq;
    }
    
    public int getBaseChance() {
        return baseChance;
    }
    
    public int getCritChance() {
        return critChance;
    }
    
    public int getMinDamage() {
        return minDamage;
    }
    
    public int getMaxDamage() {
        return maxDamage;
    }
    
    public boolean getFriendly() {
        return friendly;
    }
    
    public boolean getMulti() {
        return multi;
    }
}