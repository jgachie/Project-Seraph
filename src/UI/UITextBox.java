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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 * @author Soup
 */
public class UITextBox extends UIObject{
    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream(); //A ByteArrayOutputStream to be fed into the PrintStream
    private static final PrintStream stream = new PrintStream(baos, true); //The new PrintStream that prints to the textbox
    private static final PrintStream oldStream = System.out; //The old PrintStream
    
    private static BufferedImage image = Assets.textBox; //The image of the text box itself
    
    public UITextBox(float x, float y, int width, int height, boolean visible, BufferedImage image) {
        super(x, y, width, height, visible);
        this.image = image;
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
            
            String text = writeText(); //Write the contents of the BAOS into a text buffer
            String[] sections = text.split("\\|"); //Mostly only used during combat; a secondary text buffer that splits text up into chunks so they can be displayed to different parts of the text box
            
            //Iterate through the sections of text and print each one separately
            for (int i = 0; i < sections.length; i++){
                String[] lines = sections[i].split("\n"); //Split the section buffer into separate Strings by newline characters
                
                //Print each String on a separate line by iterating through the array and increasing the y-coordinate by the font height multiplied by the index of the array
                for (int j = 0; j < lines.length; j++){
                    //The x-coordinate increases by 250 with each section to create a clear divide
                    g.drawString(lines[j], (int) x + 20 + (500 * i), (int) y + 30 + (g.getFontMetrics().getHeight() * j));
                }
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