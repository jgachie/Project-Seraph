/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enums;

/**
 *
 * @author Soup
 */
public enum StatusEffect {
    //PERSISTENT EFFECTS
    POISON ("Poison", new String[]{}, new int[]{}), //Damages the Actor for a few hitpoints every turn
    TOXIC ("Toxic", new String[]{}, new int[]{}), //More effective version of poison
    STUN ("Stun", new String[]{}, new int[]{}), //Paralyzes the Actor, making them incapable of taking a turn
    FREEZE ("Freeze", new String[]{}, new int[]{}), //Same thing as stun, but also hurts the Actor for a few hitpoints every turn
    
    //TEMPORARY EFFECTS
    DRAGON_SKIN ("Dragon Skin", new String[]{"Defense"}, new int[]{3}), //Only applied on Sariel; increases defense by 3 and adds recoil damage to physical attackers
    
    //MISC
    NONE (null, new String[]{}, new int[]{}); //Only ever applied to weapons; just means that the weapon doesn't inflict any kind of status effect
    
    private final String value;
    private final String[] stats;
    private final int[] modifiers;
    
    private StatusEffect(String value, String[] stats, int[] modifiers){
        this.value = value;
        this.stats = stats;
        this.modifiers = modifiers;
    }
    
    public String getValue(){
        return value;
    }
    
    public String[] getStats(){
        return stats;
    }
    
    public int[] getModifiers(){
        return modifiers;
    }
}
