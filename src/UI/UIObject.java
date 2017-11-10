/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package UI;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * Essentially the menu state version of Entity class; holds all buttons, sliders, etc. found on menu
 * @author Soup
 */
public abstract class UIObject implements Serializable{
    protected float x, y; //Object coordinates
    protected int width, height; //Object width/height
    protected Rectangle bounds; //Bounding box for object
    protected boolean visible; //Whether the object is visible or not
    protected boolean hovering = false; //Whether or not the mouse is hovering over the object
    
    public UIObject(float x, float y, int width, int height, boolean visible){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visible = visible;
        
        bounds = new Rectangle((int) x, (int) y, width, height); //Set the bounding box for the object
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
    
    public abstract void onClick();
    
    /**
     * Polls mouse whenever it's moved to see if it's hovering over the object
     * @param e The mouse event
     */
    public void onMouseMove(MouseEvent e){
        //If the mouse is within the object's bounding box, set the "hovering" boolean to true; otherwise, set it to false
        if (bounds.contains(e.getX(), e.getY()))
            hovering = true;
        else
            hovering = false;
    }
    
    /**
     * Checks to see if an object has been clicked
     * @param e The mouse event
     */
    public void onMouseRelease(MouseEvent e){
        //If the mouse is hovering over the object when the mouse button is released, run the click function
        if (hovering)
            onClick();
    }
    
    //GETTERS/SETTERS
    
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
    
    public boolean isHovering() {
        return hovering;
    }
    
    public void setHovering(boolean hovering) {
        this.hovering = hovering;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}