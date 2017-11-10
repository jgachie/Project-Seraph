/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Graphics;

import java.awt.image.BufferedImage;

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
    private boolean single; //Whether the animation runs a single time or continuously
    private boolean completed; //Whether the animation has completed or not (always false for continous animations)
    
    public Animation(int speed, boolean single, BufferedImage[] frames){
        this.speed = speed;
        this.single = single;
        this.frames = frames;
        this.completed = false; //All animations start off as incomplete
        this.index = 0; //Set index to first frame
        this.timer = 0; //Initialzie timer
        this.lastTime = System.currentTimeMillis(); //Get number of milliseconds elapse since start of program
    }
    
    public void tick(){
        timer += System.currentTimeMillis() - lastTime; //Add time passed since last tick
        lastTime = System.currentTimeMillis(); //Reset tick marker
        
        //If enough time has passed relative to speed, advance to next frame and reset timer
        if (timer > speed){
            index++;
            timer = 0;
            
            //If the last frame has been reached, reset to the first frame
            if (index >= frames.length){
                index = 0;
                
                //If the animation is only supposed to run once, set completed to true
                if (single)
                    completed = true;
            }
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
     * Returns the last frame of the animation; usually used for single-run animations where the last
     * frame needs to be rendered continously
     * @return The last frame of the animation
     */
    public BufferedImage getLastFrame(){
        return frames[frames.length - 1];
    }
    
    //GETTERS/SETTERS

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}