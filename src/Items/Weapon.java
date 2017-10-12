/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Graphics.Assets;
import Items.Item;
import Misc.DamageType;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Weapon extends Item{
    //Hard-code all different types of weapons here
    public static Weapon[] weapons = new Weapon[256];
    public static Weapon broadsword = new Weapon(Assets.stone, "Broadsword", 0, 10, DamageType.SLASH);
    
    //Class
    protected final int baseDamage;
    protected final DamageType type;
    
    public Weapon(BufferedImage texture, String name, int ID, int baseDamage, DamageType type) {
        super(texture, name, ID);
        this.baseDamage = baseDamage;
        this.type = type;
    }
    
    /**
     * Returns the base damage of the weapon
     * @return The base damage of the weapon
     */
    public int getBaseDamage(){
        return baseDamage;
    }
    
    /**
     * Returns the damage type of the weapon
     * @return The damage type of the weapon
     */
    public DamageType getType(){
        return type;
    }
}