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
    public boolean up, down, left, right;
    
    public KeyManager(){
        keys = new boolean[256]; //Initialize controls array
    }
    
    /**
     * Constantly polls keys in realtime to see which ones are being pressed
     */
    public void tick(){
        up = keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_RIGHT];
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()] = true; //Tells when a key is being pressed by getting the key code of the event and setting key index to true
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()] = false; //Tells when a key is being released by getting the key code of the event and setting key index to false
    }
    
    @Override
    public void keyTyped(KeyEvent e){
        
    }
    
}
