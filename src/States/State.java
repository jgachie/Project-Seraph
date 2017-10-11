/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Main.Handler;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public abstract class State {
    private static State currentState = null; //The state the game is currently running on
    
    /**
     * Sets the current state
     * @param state The state to be set to current
     */
    public static void setState(State state){
        currentState = state;
    }
    
    /**
     * Returns the current state
     * @return The current state
     */
    public static State getState(){
        return currentState;
    }
    
    //Class
    protected Handler handler; //The game itself
    
    public State(Handler handler){
        this.handler = handler;
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
}
