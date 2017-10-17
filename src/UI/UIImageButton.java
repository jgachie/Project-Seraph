/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package UI;

import Graphics.Assets;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class UIImageButton extends UIObject{
    private String text; //The text on the button
    private BufferedImage image; //The image associated with this button; should change to a single image, since the image itself doesn't change anymore (the text does)
    private ClickListener clicker; //A click listener to handle click events
    
    public UIImageButton(String text, float x, float y, int width, int height, boolean visible,
            BufferedImage image, ClickListener clicker){
        super(x, y, width, height, visible);
        this.text = text;
        this.image = image;
        this.clicker = clicker;
    }
    
    @Override
    public void tick() {
        
    }
    
    @Override
    public void render(Graphics g) {
        //If the button is visible, render it; otherwise, do nothing
        if (visible){
            g.setFont(new Font("Felix Titling", Font.BOLD, 20)); //Set the font of the String to be drawn
            g.drawImage(image, (int) x, (int) y, width, height, null);
            
            //If the mouse is hovering over the button, draw the "selected" version of the text; otherwise, draw the normal version
            if (hovering)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);
            
            
            //All of the math here is responsible for centering the text in the button
            g.drawString(text, (int) x + (width - g.getFontMetrics().stringWidth(text)) / 2,
                    (int) y + ((height - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent());
        }
    }
    
    /**
     * Runs click action when button is clicked on
     */
    @Override
    public void onClick() {
        //Only run the click action if the button is visible
        if (visible)
            clicker.onClick();
    }
}