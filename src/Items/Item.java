/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Main.Handler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public abstract class Item implements Serializable{
    public static final int ITEM_WIDTH = 32, ITEM_HEIGHT = 32; //Default item width/height
    
    protected Handler handler; //The handler object
    protected transient BufferedImage texture; //The image used to represent the item
    protected String name; //The name of the item
    protected final String ID; //An ID unique to each type of item; the first character should be a char, denoting the type of item, followed by a number denoting its ID
    
    protected int count; //The quantity of the item that the player currently has in their inventory
    
    protected Item(BufferedImage texture, String name, String ID){
        this.texture = texture;
        this.name = name;
        this.ID = ID;
        count = 1;
    }
    
    public void tick(){
        
    }
    
    public void render(Graphics g){
        
    }
    
    /**
     * Called when an item is used; subtracts one from the player's inventory count
     */
    protected void useItem(){
        count--; //Subtract one of the item from the player's inventory count
        
        //If the item's count falls to or below 0, remove the item from the player's inventory and reset the count to 0
        if (count <= 0){
            handler.getWorld().getEntityManager().getPlayer().getInventory().removeItem(this);
            count = 0;
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getID() {
        return ID;
    }
}