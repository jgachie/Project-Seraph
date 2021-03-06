/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures;

import Entities.Entity;
import Main.Handler;
import Tiles.Tile;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public abstract class Creature extends Entity{
    public static final float DEFAULT_SPEED = 3.0f; //Creature default speed
    public static final int DEFAULT_CREATURE_WIDTH = 32, DEFAULT_CREATURE_HEIGHT = 48; //Creature default width/height
    
    protected float speed; //How fast the creature moves
    protected float xMove, yMove; //Movement buffers; holds amount of pixels moved and is added to position later to show movement
    protected int steps = 0; //The amount of steps the Player has taken since the last encounter
    
    private float xCounter, yCounter; //The number of x/y-coordinates the player has moved since the last step
    
    protected Creature(Handler handler, float x, float y, int width, int height){
        super(handler, x, y, width, height);
        
        this.speed = DEFAULT_SPEED;
        this.xMove = 0;
        this.yMove = 0;
        this.xCounter = 0;
        this.yCounter = 0;
    }
    
    /**
     * Facilitates creature movement
     */
    public void move(){
        //Check for collisions before moving
        if (!checkEntityCollisions(xMove, 0f))
            moveX();
        if (!checkEntityCollisions(0f, yMove))
            moveY();
    }
    
    /**
     * Facilitates horizontal creature movement
     */
    public void moveX(){
        //Moving right
        if (xMove > 0){
            //Don't need to check for offscreen movement; already taken care of by default tile
            
            int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH; //The target x-coordinate; where the creature is trying to move to
            
            /*
            Checks target x-coordinate (tx) against the top-right (y + bounds.y) and bottom-right (y +
            bounds.y + bounds.height) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)){
                x += xMove; //Increase x-coordinate by amount defined in buffer
                xCounter += xMove; //Increase xCounter by the same amount
                
                int tilesWalked = (int) (xCounter / Tile.TILE_WIDTH); //Get the number of tiles walked so far by dividing the number of spaces moved by the width of a tile
                //When the number of tiles walked reaches 1, increment steps taken and reset the counter
                if (Math.abs(tilesWalked) >= 1){
                    steps++;
                    xCounter = 0;
                }
            }
            //If there is a collision, set x-coordinate of bounding box to be adjacent to tile
            else
                x = tx * Tile.TILE_WIDTH - bounds.x - bounds.width - 1; //Subtract one to account for vertical movement along tile
        }
        //Moving left
        else if (xMove < 0){
            //If the player tries to move offscreen, return **NOTE: Needs to be tweaked to be more precise, a few pixels still stray offscreen as it is now
            if (x - xMove < 0.0)
                return;
            
            int tx = (int) (x + xMove + bounds.x) / Tile.TILE_WIDTH; //The target x-coordinate; where the creature is trying to move to
            
            /*
            Checks target x-coordinate (tx) against the top-left (y + bounds.y) and bottom-left (y +
            bounds.y + bounds.height) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)){
                x += xMove; //Increase x-coordinate by amount defined in buffer
                xCounter += xMove; //Increase xCounter by the same amount
                
                int tilesWalked = (int) (xCounter / Tile.TILE_WIDTH); //Get the number of tiles walked so far by dividing the number of spaces moved by the width of a tile
                //When the number of tiles walked reaches 1, increment steps taken and reset the counter
                if (Math.abs(tilesWalked) >= 1){
                    steps++;
                    xCounter = 0;
                }
            }
            //If there is a collision, set x-coordinate of bounding box to be adjacent to tile
            else
                x = tx * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x; //Moving left requires no accounting for vertical movement
        }
    }
    
    /**
     * Facilitates vertical creature movement
     */
    public void moveY(){
        //Moving up
        if (yMove < 0){
            //If the player tries to move offscreen, return **NOTE: Needs to be tweaked to be more precise, a few pixels still stray offscreen as it is now
            if (y - yMove < 0.0)
                return;
            
            int ty = (int) (y + yMove + bounds.y) / Tile.TILE_HEIGHT; //The target y-coordinate; where the creature is trying to move to
            
            /*
            Checks target y-coordinate (ty) against the top-left (x + bounds.x) and top-right (x +
            bounds.x + bounds.width) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile((int) (x + bounds.x) / Tile.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILE_WIDTH, ty)){
                y += yMove; //Increase y-coordinate by amount defined in buffer
                yCounter += yMove; //IncreaseyxCounter by the same amount
                
                int tilesWalked = (int) (yCounter / Tile.TILE_HEIGHT); //Get the number of tiles walked so far by dividing the number of spaces moved by the height of a tile
                //When the number of tiles walked reaches 1, increment steps taken and reset the counter
                if (Math.abs(tilesWalked) >= 1){
                    steps++;
                    yCounter = 0;
                }
            }
            //If there is a collision, set y-coordinate of bounding box to be adjacent to tile
            else
                y = ty * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT; //Moving up requires no accounting for horizontal movement
        }
        //Moving down
        else if (yMove > 0){
            //Don't need to check for offscreen movement; already taken care of by default tile
            
            int ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILE_HEIGHT; //The target y-coordinate; where the creature is trying to move to
            
            /*
            Checks target y-coordinate (ty) against the bottom-left (x + bounds.x) and bottom-right
            (x + bounds.x + bounds.width) corners of the colliding tile; if there's no collision (the
            tile isn't solid), the creature moves.
            */
            if (!collisionWithTile((int) (x + bounds.x) / Tile.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILE_WIDTH, ty)){
                y += yMove; //Increase y-coordinate by amound defined in buffer
                yCounter += yMove; //IncreaseyxCounter by the same amount
                
                int tilesWalked = (int) (yCounter / Tile.TILE_HEIGHT); //Get the number of tiles walked so far by dividing the number of spaces moved by the height of a tile
                //When the number of tiles walked reaches 1, increment steps taken and reset the counter
                if (Math.abs(tilesWalked) >= 1){
                    steps++;
                    yCounter = 0;
                }
            }
            //If there is a collision, set y-coordinate of bounding box to be adjacent to tile
            else
                y = ty * Tile.TILE_HEIGHT - bounds.y - bounds.height - 1; //Subtract one to account for horizontal movement along tile
        }
    }
    
    /**
     * Handles collisions between creatures and tiles; if the tile is solid, the creature will stop
     * moving
     * @param x The x-coordinate of the tile being collided with
     * @param y The y-coordinate of the tile being collided with
     * @return True if tile is solid; false if otherwise
     */
    protected boolean collisionWithTile(int x, int y){
        return handler.getWorld().getTile(x, y).isSolid(); //Return solidity of tile
    }
    
    //Getters/Setters
    public float getSpeed() {
        return speed;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public float getxMove() {
        return xMove;
    }
    
    public void setxMove(float xMove) {
        this.xMove = xMove;
    }
    
    public float getyMove() {
        return yMove;
    }
    
    public void setyMove(float yMove) {
        this.yMove = yMove;
    }
}