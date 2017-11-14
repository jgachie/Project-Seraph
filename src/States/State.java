/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Main.Handler;
import java.awt.Graphics;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public abstract class State implements Serializable{
    private static State currentState = null; //The state the game is currently running on
    private static State previousState = null; //The state the game was previously running on
    
    /**
     * Sets the current state
     * @param state The state to be set to current
     */
    public static void setState(State state){
        previousState = currentState;
        currentState = state;
    }
    
    /**
     * Sets the current state to the previous state, and the previous state to the (former) current state
     */
    public static void restoreState(){
        State tempState = currentState;
        currentState = previousState;
        previousState = tempState;
    }
    
    /**
     * Returns the current state
     * @return The current state
     */
    public static State getState(){
        return currentState;
    }
    
    /**
     * Returns the previous state
     * @return The previous state
     */
    public static State getPrevoiusState(){
        return previousState;
    }
    
    //Class
    protected Handler handler; //The handler
    
    public State(Handler handler){
        this.handler = handler;
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
}
