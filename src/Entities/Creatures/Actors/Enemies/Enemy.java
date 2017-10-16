/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Creatures.Actors.Enemies;

import Entities.Creatures.Actors.Actor;
import Items.Equipment.Weapon;
import Main.Handler;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public abstract class Enemy extends Actor{
    
    protected Enemy(Handler handler, float x, float y, int width, int height, String name, Weapon weapon,
            int level, int hitpoints, int mana, int exp, int strength, int dexterity, int wisdom, int intelligence,
            int luck, int defense, int agility){
        super(handler, x, y, width, height, name, weapon, level, hitpoints, mana, exp, strength, dexterity,
                wisdom, intelligence, luck, defense, agility);
    }
    
    public abstract void decide();
}