/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Graphics.Assets;
import Items.Item;
import Misc.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Weapon extends Item{
    //Hard-code all different types of weapons here
    public static Weapon[] weapons = new Weapon[256];
    public static Weapon bareHands = new Weapon(Assets.stone, "Bare Hands", 0, 1, 3, DamageType.CRUSH);
    public static Weapon broadsword = new Weapon(Assets.stone, "Broadsword", 1, 3, 6, DamageType.SLASH);
    
    //Class
    protected final int minDamage, maxDamage; //The lowest and highest amounts of damage the weapon itself can deal
    protected final DamageType type; //The type of damage the weapon deals
    protected final StatusEffect effect; //The type of status effect the weapon deals, if any at all
    
    private Weapon(BufferedImage texture, String name, int ID, int minDamage, int maxDamage, DamageType type,
            StatusEffect effect) {
        super(texture, name, ID);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.type = type;
        this.effect = effect;
    }
    
    private Weapon(BufferedImage texture, String name, int ID, int minDamage, int maxDamage, DamageType type){
        super(texture, name, ID);
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.type = type;
        this.effect = StatusEffect.NONE;
    }
    
    /**
     * Returns the minimum amount of damage the weapon can deal
     * @return The minimum amount of damage the weapon can deal
     */
    public int getMinDamage(){
        return minDamage;
    }
    
    /**
     * Returns the maximum amount of damage the weapon can deal
     * @return The maximum amount of damage the weapon can deal
     */
    public int getMaxDamage(){
        return maxDamage;
    }
    
    /**
     * Returns the damage type of the weapon
     * @return The damage type of the weapon
     */
    public DamageType getType(){
        return type;
    }
    
    /**
     * Returns the type of status effect the weapon inflicts
     * @return The type of status effect the weapon inflicts
     */
    public StatusEffect getEffect(){
        return effect;
    }
}