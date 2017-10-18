/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package UI;

import Graphics.Assets;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Soup
 */
public class UITextBox extends UIObject{
    private static ByteArrayOutputStream baos = new ByteArrayOutputStream(); //A ByteArrayOutputStream to be fed into the PrintStream
    private static PrintStream stream = new PrintStream(baos, true); //The new PrintStream that prints to the textbox
    private static PrintStream oldStream = System.out; //The old PrintStream
    
    private BufferedImage image; //The image of the text box itself
    private Graphics2D g2; //A Graphics2D object to hold the BufferedImage's graphics
    
    public UITextBox(float x, float y, int width, int height, boolean visible, BufferedImage image) {
        super(x, y, width, height, visible);
        this.image = image;
        this.g2 = image.createGraphics();
    }
    
    @Override
    public void tick() {
    }
    
    @Override
    public void render(Graphics g) {
        //Only render the text box if it's currently visible
        if (visible){
            g.drawImage(image, (int) x, (int) y, width, height, null); //Render the text box
            
            g.setColor(Color.white); //Set the text color
            g.setFont(new Font("Felix Titling", Font.BOLD, 18)); //Set the text font
            
            String text = writeText();
            
            String[] lines = text.split("\n"); //Split the text buffer into separate Strings by newline characters
            
            //Print each String on a separate line by iterating through the array and increasing the y-coordinate by the font height multiplied by the index of the array
            for (int i = 0; i < lines.length; i++){
                g.drawString(lines[i], (int) x + 20, (int) y + 30 + (g.getFontMetrics().getHeight() * i));
            }
        }
    }
    
    @Override
    public void onClick() {}
    
    /**
     * Resets the ByteArrayOutputStream so that old text doesn't get printed again; should be called
     * before printing a new set of text to the text box
     */
    public static void resetBAOS(){
        baos.reset();
    }
    
    /**
     * Returns the contents of the ByteArrayOutputStream as a String so it can be printed to the text box
     * @return A String containing the contents of the ByteArrayOutputStream
     */
    private String writeText(){
        return baos.toString();
    }
    
    public static PrintStream getStream() {
        return stream;
    }
}