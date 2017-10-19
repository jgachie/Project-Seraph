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
    protected final String ID; //An ID unique to each type of item; the first character should be a char, denoting the type of item, followed by a number denoting its ID
    
    protected int numHeld; //The quantity of the item that the player currently has in their inventory
    
    public Item(BufferedImage texture, String name, String ID){
        this.handler = handler;
        this.texture = texture;
        this.name = name;
        this.ID = ID;
        numHeld = 0;
    }
}
