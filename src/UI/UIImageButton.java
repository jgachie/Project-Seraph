/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class UIImageButton extends UIObject{
    private String text; //The text on the button
    private static transient BufferedImage image; //The image associated with this button; should change to a single image, since the image itself doesn't change anymore (the text does)
    private ClickListener clicker; //A click listener to handle click events
    private boolean clickable; //Whether the button is clickable or not
    
    public UIImageButton(String text, float x, float y, int width, int height, boolean visible, boolean clickable,
            BufferedImage image, ClickListener clicker){
        super(x, y, width, height, visible);
        this.clickable = clickable;
        this.text = text;
        this.image = image;
        this.clicker = clicker;
    }
    
    //For buttons that default to being clickable from the beginning
    public UIImageButton(String text, float x, float y, int width, int height, boolean visible, BufferedImage image,
            ClickListener clicker){
        super(x, y, width, height, visible);
        this.clickable = true;
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
            g.setFont(new Font("Felix Titling", Font.BOLD, 18)); //Set the font of the String to be drawn
            g.drawImage(image, (int) x, (int) y, width, height, null);
            
            //If the mouse is hovering over the button (and it's clickable), draw the "selected" version of the text; otherwise, draw the normal version
            if (hovering && clickable)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);
            
            float textWidth = g.getFontMetrics().stringWidth(text); //Get the width of the button's text
            
            /*
            Mostly provisionay; bascially, checks to see if the width of the button's text is greater
            than the width of the button. If so, the text is split into a seprate piece at each space
            and drawn procedurally. If not, the text is drawn normally.
            */
            if (textWidth > width){
                String[] textBuffer = text.split(" ");
                
                for (int i = 0; i < textBuffer.length; i++){
                    g.drawString(textBuffer[i], (int) x + (width - g.getFontMetrics().stringWidth(textBuffer[i])) / 2,
                            (int) y + g.getFontMetrics().getHeight() * (i + 1) + 8 );
                }
            }
            else{
                //All of the math here is responsible for centering the text in the button
                g.drawString(text, (int) x + (width - g.getFontMetrics().stringWidth(text)) / 2,
                        (int) y + ((height - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent());
            }
        }
    }
    
    /**
     * Runs click action when button is clicked on
     */
    @Override
    public void onClick() {
        //Only run the click action if the button is visible
        if (visible && clickable)
            clicker.onClick();
    }
    
    public boolean isClickable() {
        return clickable;
    }
    
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}