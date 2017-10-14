/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Main.Handler;
import java.awt.*;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public abstract class Entity implements Serializable{
    protected transient Handler handler; //The game itself
    protected float x, y; //X and Y positions
    protected int width, height; //Width/height of the entity
    protected boolean active = true; //If true, the entity is still on screen and should be ticked and rendered; if not, it's removed from the entity list and ignored
    protected Rectangle bounds; //Bounding box for collisions
    
    protected Entity(Handler handler, float x, float y, int width, int height){
        this.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        bounds = new Rectangle(0, 0, width, height);
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
    
    /**
     * Polls all other entities in entity list to see if they've collided with this specific entity
     * @param xOffset The target x-coordinate of the moving entity
     * @param yOffset The target y-coordinate of the moving entity
     * @return True if there is a collision; false if otherwise
     */
    public boolean checkEntityCollisions(float xOffset, float yOffset){
        //Iterate through entire entity list
        for (Entity e : handler.getWorld().getEntityManager().getEntities()){
            //Make sure the entity isn't tested against itself
            if (e.equals(this))
                continue;
            //If the entity's bounding box intersects with that of another entity, return true
            if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset)))
                return true;
        }
        return false; //Return false if no collision was detected
    }
    
    /**
     * Fetches entity's bounding box
     * @param xOffset
     * @param yOffset
     * @return A Rectangle delineating the entity's bounding box
     */
    public Rectangle getCollisionBounds(float xOffset, float yOffset){
        return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}