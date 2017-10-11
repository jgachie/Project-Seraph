/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Soup
 */
public class Animation implements Serializable{
    private int speed; //The speed of the animation
    private int index; //The number of the current frame
    private long timer; //Keeps track of time over whole animation
    private long lastTime; //Keeps track of milliseconds passed since last tick
    private transient BufferedImage[] frames; //The set of frames to be animated
    
    public Animation(int speed, BufferedImage[] frames){
        this.speed = speed;
        this.frames = frames;
        index = 0; //Set index to first frame
        timer = 0; //Initialzie timer
        lastTime = System.currentTimeMillis(); //Get number of milliseconds elapse since start of program
    }
    
    public void tick(){
        timer += System.currentTimeMillis() - lastTime; //Add time passed since last tick
        lastTime = System.currentTimeMillis(); //Reset tick marker
        
        //If enough time has passed relative to speed, advance to next frame and reset timer
        if (timer > speed){
            index++;
            timer = 0;
            
            //If the last frame has been reached, reset to the first frame
            if (index >= frames.length)
                index = 0;
        }
    }
    
    /**
     * Returns the current frame of the animation
     * @return The current frame
     */
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }
    
    /**
     * Specialized override method for serializing BufferedImage array of animation frames
     * @param objectOut The Object Output Stream
     * @throws IOException 
     */
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.defaultWriteObject(); //Serialize all static and non-transient fields; basically, serialize everything else normally
        out.writeInt(frames.length); //Write a 32-bit int to store the number of BufferedImages to be serialized
        
        //Write each individual BufferedImage as a separate .png image to the serialized file
        for (BufferedImage frame : frames)
            ImageIO.write(frame, "png", out);
    }
    
    /**
     * Specialized override method for deserializing BufferedImage array of animation frames
     * @param objectIn
     * @throws IOException 
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
        in.defaultReadObject(); //Deserialize all static and non-transient fields; basically, deserialize everything else normally
        final int frameCount = in.readInt(); //Read from the 32-bit int written to when serializing to get number of BufferedImages serialized
        frames = new BufferedImage[frameCount]; //Reinitialize "frames" with length from read int
        
        //Reassign each saved image to their position in BufferdImage array
        for (int i = 0; i < frames.length; i++)
            frames[i] = ImageIO.read(in);
    }
}
