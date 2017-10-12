/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graphics;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Soup
 */
public class Animation {
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
}