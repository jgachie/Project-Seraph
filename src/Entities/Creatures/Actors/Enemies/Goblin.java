/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors.Enemies;

import Entities.Creatures.Actors.Actor;
import Enums.Characters;
import Graphics.Assets;
import Items.Equipment.Weapon;
import Main.Handler;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public class Goblin extends Enemy{
    
    public Goblin(Handler handler){
        super(handler, 0, 0, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT, "Goblin", Weapon.broadsword,
                1, 100, 0, 50, 5, 5, 5, 5, 5, 5, 5);
        
        //Set bounding box coordinates (relative to top-left corner of entity) and width/height
        bounds.x = 8;
        bounds.y = 12;
        bounds.width = 16;
        bounds.height = 28;
    }

    @Override
    public void decide() {
        
    }
    
    @Override
    public void tick() {
        
    }
    
    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.goblin, (int) x, (int) y, width, height, null);
    }
}
