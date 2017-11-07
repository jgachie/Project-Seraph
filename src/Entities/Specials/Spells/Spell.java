/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Spells;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Special;
import Main.Handler;

/**
 *
 * @author Soup
 */
public abstract class Spell extends Special{
    
    protected Spell(String name, String desc, int pointReq, int baseChance, int critChance, int minDamage,
            int maxDamage, boolean friendly, boolean multi, int width, int height){
        super(0, 0, width, height, name, desc, pointReq, baseChance, critChance, minDamage, maxDamage,
                friendly, multi);
    }
    
    /**
     * Casts the spell
     * @param caster The Actor casting the spell
     * @param target The Actor being targeted
     */
    public void cast(PlayableActor caster, Actor target, Handler handler){
        this.user = caster;
        this.target = target;
        this.handler = handler;
    }
    
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
        else if (wisdom >= 50)
            damage = (int) (baseDamage * (wisdom / 20.0) + (baseDamage * 4.9));
        
        //Only run critical hit calculations if critChance isn't 0
        if (critChance > 0){
            //If it was a critical hit, multiply the damage by 1.5
            if (spellCrit(user)){
                System.out.println("It was a critical hit!");
                damage *= 1.5;
            }
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
}