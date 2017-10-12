/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Soup
 */
public class KeyManager implements KeyListener{
    private boolean[] keys; //Array of keys mapped to controls in the game
    private boolean[] justPressed, cantPress; //Arrays to hold keys that were just pressed and keys that can't be pressed
    public boolean up, down, left, right;
    
    public KeyManager(){
        //Intialize key arrays
        keys = new boolean[256];
        justPressed = new boolean[256];
        cantPress = new boolean[256];
    }
    
    /**
     * Constantly polls keys in realtime to see which ones are being pressed
     */
    public void tick(){
        //For each key, define what it means to be "just pressed"
        for (int i = 0; i < keys.length; i++){
            //If the player currently can't press the key but it's already been released, make the key pressable again
            if (cantPress[i] && !keys[i])
                cantPress[i] = false;
            
            //If the key has been registered as being pressed already, make it unpressable and set justPressed to false
            else if (justPressed[i]){
                cantPress[i] = true;
                justPressed[i] = false;
            }
            
            //If the key is pressable and currently being pressed, set justPressed to true
            else if (!cantPress[i] && keys[i])
                justPressed[i] = true;
        }
        
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
    }
    
    /**
     * Determines whether or not a key has been "just pressed" (pressed, but not held)
     * @param keyCode The numerical code of the key that's been pressed
     * @return Whether or not the key has been pressed
     */
    public boolean keyJustPressed(int keyCode){
        //Check to make sure that the keyCode is valid or we'll get errors
        if (keyCode < 0 || keyCode >= keys.length)
            return false;
        
        return justPressed[keyCode];
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        //Check to make sure the event is valid or we'll get errors
        if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
            return;
        keys[e.getKeyCode()] = true; //Tells when a key is being pressed by getting the key code of the event and setting key index to true
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        //Check to make sure the event is valid or we'll get errors
        if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length)
            return;
        keys[e.getKeyCode()] = false; //Tells when a key is being released by getting the key code of the event and setting key index to false
    }
    
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    
}
