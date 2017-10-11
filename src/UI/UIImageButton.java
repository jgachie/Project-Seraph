/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package UI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class UIImageButton extends UIObject{
    private BufferedImage[] images; //The images associated with this button
    private ClickListener clicker; //A click listener to handle click events
    
    public UIImageButton(float x, float y, int width, int height, BufferedImage[] images, ClickListener clicker){
        super(x, y, width, height);
        this.images = images;
        this.clicker = clicker;
    }
    
    @Override
    public void tick() {
        
    }
    
    @Override
    public void render(Graphics g) {
        //If the mouse is hovering over the button, draw the "selected" version of the button; otherwise, draw the normal version
        if (hovering)
            g.drawImage(images[1], (int) x, (int) y, width, height, null);
        else
            g.drawImage(images[0], (int) x, (int) y, width, height, null);
    }
    
    /**
     * Runs click action when button is clicked on
     */
    @Override
    public void onClick() {
        clicker.onClick();
    }
}