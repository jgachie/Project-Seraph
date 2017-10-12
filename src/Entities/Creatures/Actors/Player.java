/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors;

import Entities.Creatures.Creature;
import Graphics.*;
import Main.Handler;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Player extends Actor{
    //Declare animations
    private static Animation walkDown, walkUp, walkLeft, walkRight;
    
    public Player(Handler handler, float x, float y, String name){
        super(handler, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT, name, /*weapon*/ 1, 100,
                100, 5, 5, 5, 5, 5, 5, 5, 5, true);
        
        //Set bounding box coordinates (relative to top-left corner of player entity) and width/height
        bounds.x = 8;
        bounds.y = 12;
        bounds.width = 16;
        bounds.height = 28;
        
        //Initialize animations
        walkDown = new Animation(150, Assets.player_down);
        walkUp = new Animation(150, Assets.player_up);
        walkLeft = new Animation(150, Assets.player_left);
        walkRight = new Animation(150, Assets.player_right);
    }
    
    @Override
    public void tick(){
        //Tick animations
        walkDown.tick();
        walkUp.tick();
        walkLeft.tick();
        walkRight.tick();
        
        //Movements
        getInput(); //Get player input
        move(); //Apply input and move player entity
        handler.getGameCamera().centerOnEntity(this); //Center camera on player entity
    }
    
    /**
     * Manages input for player entity
     */
    private void getInput(){
        //Reset movement buffers
        xMove = 0;
        yMove = 0;
        
        //Movement controls; buffer is changed in increments defined by speed
        if (handler.getKeyManager().up)
            yMove = -speed;
        if (handler.getKeyManager().down)
            yMove = speed;
        if (handler.getKeyManager().left)
            xMove = -speed;
        if (handler.getKeyManager().right)
            xMove = speed;
    }
    
    @Override
    public void render(Graphics g){
        //Subtract offset from position to focus camera on player
        g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getXOffset()),
                (int) (y - handler.getGameCamera().getYOffset()), width, height, null);
    }
    
    private BufferedImage getCurrentAnimationFrame(){
        if (xMove < 0)
            return walkLeft.getCurrentFrame();
        else if (xMove > 0)
            return walkRight.getCurrentFrame();
        else if (yMove < 0)
            return walkUp.getCurrentFrame();
        else if (yMove > 0)
            return walkDown.getCurrentFrame();
        else{
            return Assets.player;
        }
    }
    
    /**
     * Player-specific save method; calls the save supermethod with a specific filename 
     */
    public void save(){
        save(this, "Sariel");
    }
    
    /**
     * Player-specific load method; calls the load supermethod with a specific filename
     * @param handler A handler object to replace the one that wasn't serialized
     * @return The loaded Player object
     */
    public static Player load(Handler handler){
        Player player = (Player) Actor.load("Sariel");
        player.setHandler(handler);
        return player;
    }
    
    private void setHandler(Handler handler){
        this.handler = handler;
    }
}