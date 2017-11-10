/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.UsableItems;

import Entities.Creatures.Actors.Actor;

/**
 *
 * @author Soup
 */
public class Potion extends UsableItem{
    private static final int RESTORE = 30; //The amount of hitpoints that the potion restores
    
    public Potion(){
        super(null, "Potion", "I001");
    }

    @Override
    public void use(Actor user, Actor target) {
        super.use(user, target);
        
        target.heal(RESTORE); //Heal the target
    }
}