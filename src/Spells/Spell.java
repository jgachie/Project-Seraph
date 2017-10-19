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

/**
 *
 * @author Soup
 */
public abstract class Spell implements Serializable{
    protected final String name; //The name of the spell
    protected final String desc; //The spell's description
    protected final int manaReq; //The amount of mana required to cast the spell
    protected final int baseChance; //The base chance of the spell succeeding
    protected final int critChance; //The base chance of the spell getting a critical hit
    protected final int baseDamage; //The base amount of damage that the spell deals
    
    protected Spell(String name, String desc, int manaReq, int baseChance, int critChance, int baseDamage){
        this.name = name;
        this.desc = desc;
        this.manaReq = manaReq;
        this.baseChance = baseChance;
        this.critChance = critChance;
        this.baseDamage = baseDamage;
    }
    
    /**
     * Casts the spell
     * @param caster The Actor casting the spell
     * @param target The Actor being targeted
     */
    public abstract void cast(Actor caster, Actor target);
    
    /**
     * Tests intelligence against given base chance of success to determine whether a spell hits or 
     * not
     * @param baseChance The spell's base chance of success
     * @return True if the spell hit; false if it missed
     */
    protected boolean spellHit(Actor caster, int baseChance){
        
        //Will implement later
        return true;
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
    
    public int getBaseDamage() {
        return baseDamage;
    }
}