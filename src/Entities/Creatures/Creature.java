/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures;

import Entities.Entity;
import Main.Handler;
import Tiles.Tile;

/**
 *
 * @author Soup
 */
public abstract class Creature extends Entity{
    public static final float DEFAULT_SPEED = 3.0f; //Creature default speed
    public static final int DEFAULT_CREATURE_WIDTH = 32, DEFAULT_CREATURE_HEIGHT = 48; //Creature default width/height
    
    protected float speed; //How fast the creature moves
    protected float xMove, yMove; //Movement buffers; holds amount of pixels moved and is added to position later to show movement
    
    public Creature(Handler handler, float x, float y, int width, int height){
        super(handler, x, y, width, height);
        speed = DEFAULT_SPEED;
        xMove = 0;
        yMove = 0;
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
            int tx = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH; //The target x-coordinate; where the creature is trying to move to
            
            /*
            Checks target x-coordinate (tx) against the top-right (y + bounds.y) and bottom-right (y +
            bounds.y + bounds.height) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)){
                x += xMove; //Increase x-coordinate by amount defined in buffer
            }
            //If there is a collision, set x-coordinate of bounding box to be adjacent to tile
            else{
                x = tx * Tile.TILE_WIDTH - bounds.x - bounds.width - 1; //Subtract one to account for vertical movement along tile
            }
        }
        //Moving left
        else if (xMove < 0){
            int tx = (int) (x + xMove + bounds.x) / Tile.TILE_WIDTH; //The target x-coordinate; where the creature is trying to move to
            
            /*
            Checks target x-coordinate (tx) against the top-left (y + bounds.y) and bottom-left (y +
            bounds.y + bounds.height) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile(tx, (int) (y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)){
                x += xMove; //Increase x-coordinate by amount defined in buffer
            }
            //If there is a collision, set x-coordinate of bounding box to be adjacent to tile
            else{
                x = tx * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x; //Moving left requires no accounting for vertical movement
            }
        }
    }
    
    /**
     * Facilitates vertical creature movement
     */
    public void moveY(){
        //Moving up
        if (yMove < 0){
            int ty = (int) (y + yMove + bounds.y) / Tile.TILE_HEIGHT; //The target y-coordinate; where the creature is trying to move to
            
            /*
            Checks target y-coordinate (ty) against the top-left (x + bounds.x) and top-right (x +
            bounds.x + bounds.width) corners of the colliding tile; if there's no collision (the tile
            isn't solid), the creature moves.
            */
            if (!collisionWithTile((int) (x + bounds.x) / Tile.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILE_WIDTH, ty)){
                y += yMove; //Increase y-coordinate by amound defined in buffer
            }
            //If there is a collision, set y-coordinate of bounding box to be adjacent to tile
            else{
                y = ty * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT; //Moving up requires no accounting for horizontal movement
            }
        }
        //Moving down
        else if (yMove > 0){
            int ty = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILE_HEIGHT; //The target y-coordinate; where the creature is trying to move to
            
            /*
            Checks target y-coordinate (ty) against the bottom-left (x + bounds.x) and bottom-right
            (x + bounds.x + bounds.width) corners of the colliding tile; if there's no collision (the
            tile isn't solid), the creature moves.
            */
            if (!collisionWithTile((int) (x + bounds.x) / Tile.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.x + bounds.width) / Tile.TILE_WIDTH, ty)){
                y += yMove; //Increase y-coordinate by amound defined in buffer
            }
            //If there is a collision, set y-coordinate of bounding box to be adjacent to tile
            else{
                y = ty * Tile.TILE_HEIGHT - bounds.y - bounds.height - 1; //Subtract one to account for horizontal movement along tile
            }
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