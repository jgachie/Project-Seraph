/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Skills;

import Entities.Creatures.Actors.Actor;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author Soup
 */
public abstract class Skill implements Serializable{
    protected static Random dieRoll = new Random(); //A Random object for determining various outcomes
    
    protected final String name; //The name of the skill
    protected final String desc; //The skill's description
    protected final int skillReq; //The amount of skillpoints required to use the skill
    protected final int baseChance; //The base chance of the skill succeeding
    protected final int critChance; //The base chance of the skill getting a critical hit
    protected final int minDamage, maxDamage; //The lowest and highest amounts of base damage the skill can deal
    protected final boolean friendly; //Whether or not the skill is to be used on the Player's party or the enemy's; true if the Player's party, false if the enemy's
    protected final boolean multi; //Whether the skill is to be used on a single target or multiple targets; true if multiple targets, false if only one
    
    protected Skill(String name, String desc, int skillReq, int baseChance, int critChance, int minDamage,
            int maxDamage, boolean friendly, boolean multi){
        this.name = name;
        this.desc = desc;
        this.skillReq = skillReq;
        this.baseChance = baseChance;
        this.critChance = critChance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.friendly = friendly;
        this.multi = multi;
    }
    
    //For skills that don't deal damage
    protected Skill(String name, String desc, int skillReq, int baseChance, boolean friendly, boolean multi){
        this.name = name;
        this.desc = desc;
        this.skillReq = skillReq;
        this.baseChance = baseChance;
        this.critChance = 0;
        this.minDamage = 0;
        this.maxDamage = 0;
        this.friendly = friendly;
        this.multi = multi;
    }
    
    /**
     * Uses the skill
     * @param user The Actor using the skill
     * @param target The Actor being targeted
     */
    public abstract void use(Actor user, Actor target);
    
    /**
     * Uses user's skill and skill's base damage to calculate initial damage dealt before running resistances
     * @param user The Actor using the skill
     * @return The amount of damage dealt
     */
    protected int calcDamage(Actor user){
        int wisdom = user.getWisdom();
        int damage = 0;
        int baseDamage = dieRoll.nextInt(maxDamage + 1) + minDamage; //Roll for base damage using skill's min and max damage as range
        
        //Calculate damage dealt
        if (wisdom < 25)
            damage = (int) (baseDamage * (wisdom / 5.0));
        else if (25 <= wisdom && wisdom < 50)
            damage = (int) (baseDamage * (wisdom / 10.0) + (baseDamage * 2.4));
        else if (wisdom > 50)
            damage = (int) (baseDamage * (wisdom / 20.0) + (baseDamage * 4.9));
        
        //If the skill was critical, multiply the damage by 2
        if (skillCrit(user)){
            System.out.println("It was a critical hit!");
            damage *= 2;
        }
        
        return damage;
    }
    
    /**
     * Tests intelligence against given base chance of success to determine whether a skill hits or
     * not
     * @param user The Actor using the skill
     * @return True if the skill hit; false if it missed
     */
    protected boolean skillHit(Actor user){
        boolean hit = false; //Whether or not the skill will succeed; initialized to false
        int intelligence = user.getIntelligence();
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
     * Tests luck against given critical hit chance to determine whether a skill is critical or not
     * @param user The actor using the skill
     * @return True if the skill was critical; false if it wasn't
     */
    protected boolean skillCrit(Actor user){
        boolean crit = false; //Whether or not the skill will be critical; initialized to false
        int luck = user.getLuck();
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
    
    public int getSkillReq() {
        return skillReq;
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