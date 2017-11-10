/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.UsableItems;

import Entities.Creatures.Actors.Actor;
import Items.Item;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public abstract class UsableItem extends Item{
    
    protected UsableItem(BufferedImage texture, String name, String ID){
        super(texture, name, ID);
    }
    
    public void use(Actor user, Actor target){
        useItem();
    }
}