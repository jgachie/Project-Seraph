/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Specials.Skills;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Special;
import Main.Handler;

/**
 *
 * @author Soup
 */
public abstract class Skill extends Special{
    
    protected Skill(String name, String desc, int pointReq, int baseChance, int critChance, int minDamage,
            int maxDamage, boolean friendly, boolean multi, int width, int height){
        super(0, 0, width, height, name, desc, pointReq, baseChance, critChance, minDamage, maxDamage,
                friendly, multi);
    }
    
    /**
     * Uses the skill
     * @param user The Actor using the skill
     * @param target The Actor being targeted
     */
    public void use(PlayableActor user, Actor target, Handler handler){
        this.user = user;
        this.target = target;
        this.handler = handler;
    }
    
    /**
     * Uses user's skill stat and skill's base damage to calculate initial damage dealt before running
     * resistances
     * @param user The PlayableActor using the skill
     * @return The amount of damage dealt
     */
    protected int calcDamage(PlayableActor user){
        int skill = user.getSkill();
        int damage = 0;
        int baseDamage = dieRoll.nextInt(maxDamage + 1) + minDamage; //Roll for base damage using skill's min and max damage as range
        
        //If minDamage and maxDamage are both 0, load the skill's damage values with those of the user's weapon
        if (minDamage == 0 && maxDamage == 0){
            minDamage = user.getWeapon().getMinDamage();
            maxDamage = user.getWeapon().getMaxDamage();
        }
        
        //Calculate damage dealt
        if (skill < 25)
            damage = (int) (baseDamage * (skill / 5.0));
        else if (25 <= skill && skill < 50)
            damage = (int) (baseDamage * (skill / 10.0) + (baseDamage * 2.4));
        else if (skill > 50)
            damage = (int) (baseDamage * (skill / 20.0) + (baseDamage * 4.9));
        
        //If it was a critical hit, multiply the damage by 2
        if (skillCrit(user)){
            System.out.println("It was a critical hit!");
            damage *= 2;
        }
        
        return damage;
    }
    
    /**
     * Take the average of the user's skill and dexterity stats and tests it against given base chance
     * of success to determine whether a skill hits or not
     * @param user The PlayableActor using the skill
     * @return True if the skill hit; false if it missed
     */
    protected boolean skillHit(PlayableActor user){
        boolean hit = false; //Whether or not the skill will succeed; initialized to false
        int chance = (user.getSkill() + user.getDexterity()) / 2; //The average of the user's skill and dexterity stats
        int accuracy = dieRoll.nextInt(100); //Roll for accuracy
        
        //Determine whether the hit was critical
        if (chance < 25){
            if (accuracy <= baseChance - chance)
                hit = false;
            else
                hit = true;
        }
        else if (25 <= chance && chance < 50){
            if (accuracy <= baseChance - (baseChance / (baseChance / 12)) - (chance / 2))
                hit = false;
            else
                hit = true;
        }
        else if (chance >= 50){
            if (accuracy <= baseChance - (baseChance / (baseChance / 12)) - 50)
                hit = false;
            else
                hit = true;
        }
        
        return hit;
    }
    
    /**
     * Tests luck against given critical hit chance to determine whether a skill is critical or not
     * @param user The PlayableActor using the skill
     * @return True if the skill was critical; false if it wasn't
     */
    protected boolean skillCrit(PlayableActor user){
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
}