/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items;

import Graphics.Assets;
import Main.Handler;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public abstract class Item implements Serializable{
    public static final int ITEM_WIDTH = 32, ITEM_HEIGHT = 32; //Default item width/height
    
    protected transient Handler handler; //The handler object
    protected static BufferedImage texture; //The image used to represent the item
    protected String name; //The name of the item
    protected final int ID; //An ID unique to each type of item
    
    protected int count; //The x-coordinate, y-coordinate, and quantity of the item that the player currently has in their inventory
    
    public Item(BufferedImage texture, String name, int ID){
        this.texture = texture;
        this.name = name;
        this.ID = ID;
        count = 1;
    }
}
