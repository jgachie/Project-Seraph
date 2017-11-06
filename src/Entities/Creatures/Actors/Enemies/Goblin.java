/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors.Enemies;

import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Graphics.Animation;
import Graphics.Assets;
import Items.Equipment.Weapon;
import Main.Handler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Soup
 */
public class Goblin extends Enemy{
    private static Random dieRoll = new Random(); //A Radnom object for deciding outcomes
    
    private static Animation fight;
    
    public Goblin(Handler handler, ArrayList<Enemy> enemyParty){
        super(handler, 0, 0, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT, "Goblin", Weapon.bareHands,
                1, 100, 0, 50, 5, 5, 5, 5, 5, 5, 5, enemyParty);
        
        //Set bounding box coordinates (relative to top-left corner of entity) and width/height
        bounds.x = 8;
        bounds.y = 12;
        bounds.width = 16;
        bounds.height = 28;
        
        //Initialize animations
        fight = new Animation(200, false, Assets.goblinFightLeft);
    }
    
    @Override
    public void decide(ArrayList<PlayableActor> party) {
        int target = dieRoll.nextInt(party.size());
        
        setAction(() -> {
            attack(party.get(target));
        });
    }
    
    @Override
    public void tick() {
        fight.tick();
    }
    
    @Override
    public void render(Graphics g) {
        BufferedImage frame = getCurrentAnimationFrame();
        g.drawImage(frame, (int) (x), (int) (y), frame.getWidth() * 2, frame.getHeight() * 2, null);
    }
    
    protected BufferedImage getCurrentAnimationFrame(){
        return fight.getCurrentFrame();
    }
}
