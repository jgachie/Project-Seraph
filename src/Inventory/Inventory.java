/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Inventory;

import Main.Handler;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public class Inventory implements Serializable{
    private transient Handler handler;
    private boolean active = false; //Whether the inventory should currently be open
    
    public Inventory(Handler handler){
        this.handler = handler;
    }
    
    public void tick(){
        //Toggle the active variable whenever the I key is pressed to see if the inventory should be brought up or not
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_I))
                active = !active;
        
        //If the inventory isn't active, don't tick it
        if (!active)
            return;
    }
    
    public void render(Graphics g){
        //If the inventory isn't active, don't render it
        if (!active)
            return;
    }
    
    //Getters/Setters
    public Handler getHandler() {
        return handler;
    }
    
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    
}
