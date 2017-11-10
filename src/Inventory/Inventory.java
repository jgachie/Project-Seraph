/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Inventory;

import Items.Item;
import Main.Handler;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class Inventory implements Serializable{
    private Handler handler; //The handler
    private boolean active = false; //Whether the inventory should be ticked and rendered
    private ArrayList<Item> inventory; //An ArrayList holding all of the items in the player's inventory
    
    public Inventory(Handler handler){
        this.handler = handler;
        this.inventory = new ArrayList<Item>();
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
    
    /**
     * Adds an item to the player's inventory
     * @param item The item to be added to the inventory
     */
    public void addItem(Item item){
        //Run through the inventory to see if the player is already holding an item of this type
        for (Item i : inventory){
            //If the player is holding the item, add the new instance's count to that of the old instance and return
            if (i.getID().equals(item.getID())){
                i.setCount(i.getCount() + item.getCount());
                return;
            }
        }
        
        //If the player isn't holding any of that type of item, add the new item to their inventory
        inventory.add(item);
    }
    
    /**
     * Removes an item from the player's inventory
     * @param item The item to be removed from the inventory
     */
    public void removeItem(Item item){
        //If the item is in the inventory, remove it
        if (inventory.contains(item))
            inventory.remove(item);
    }
    
    //GETTERS/SETTERS
    
    public Handler getHandler() {
        return handler;
    }
    
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    
    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }
}