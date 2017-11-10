/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Items.Equipment;

import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.StatusEffect;
import Enums.Characters;
import Enums.DamageType;
import Graphics.Assets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Soup
 */
public class Weapon extends Equipment{
    //Hard-code all different types of weapons here
    public static Weapon[] weapons = new Weapon[256];
    public static Weapon bareHands = new Weapon(Assets.stone, "Bare Hands", "W001", new int[]{1, 1, 1, 1, 1, 1, 1, 1},
            3, 6, DamageType.CRUSH);
    public static Weapon broadsword = new Weapon(Assets.stone, "Broadsword", "W002", new int[]{3, 2, 1, 1, 1, 1, 1, 1},
            6, 9, DamageType.SLASH);
    
    //Class
    protected final int minDamage, maxDamage; //The lowest and highest amounts of damage the weapon itself can deal
    protected final DamageType type; //The type of damage the weapon deals
    protected final StatusEffect effect; //The type of status effect the weapon deals, if any at all
    
    private Weapon(BufferedImage texture, String name, String ID, int[] statReqs, int minDamage, int maxDamage,
            DamageType type, StatusEffect effect, Characters... users) {
        super(texture, name, ID, statReqs, new ArrayList<Characters>(Arrays.asList(users))); //Last argument takes users array and transforms it into an ArrayList for functionality
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.type = type;
        this.effect = effect;
    }
    
    private Weapon(BufferedImage texture, String name, String ID, int[] statReqs, int minDamage, int maxDamage,
            DamageType type, Characters... users){
        super(texture, name, ID, statReqs, new ArrayList<Characters>(Arrays.asList(users))); //Last argument takes users array and transforms it into an ArrayList for functionality
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.type = type;
        this.effect = StatusEffect.NONE;
    }
    
    @Override
    public void equip(PlayableActor actor){
        if (checkChar(actor.getCharacter()) && checkStats(actor.getStats()))
            actor.setWeapon(this);
    }
    
    //GETTERS/SETTERS
    
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