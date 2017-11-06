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
    POISON, //Damages the Actor for a few hitpoints every turn
    TOXIC, //More effective version of poison
    STUN, //Paralyzes the Actor, making them incapable of taking a turn
    FREEZE, //Same thing as stun, but also hurts the Actor for a few hitpoints every turn
    
    //TEMPORARY EFFECTS
    DRAGON_SKIN, //Only applied on Sariel;
    
    //MISC
    NONE; //Only ever applied to weapons; just means that the weapon doesn't inflict any kind of status effect
}
