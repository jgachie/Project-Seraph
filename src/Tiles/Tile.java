/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Tile {
    //Tile initialization
    public static Tile[] tiles = new Tile[256]; //Array to hold one instance of every tile in the game
    public static Tile grassTile = new GrassTile(0);
    public static Tile dirtTile = new DirtTile(1);
    public static Tile stoneTile = new StoneTile(2);
    public static Tile waterTile = new WaterTile(3);
    
    //Class
    public static final int TILE_WIDTH = 32, TILE_HEIGHT = 32; //Tile width/height
    
    protected BufferedImage texture; //Image asset associated with the tile
    protected final int ID; //ID number unique to each different tile
    
    public Tile(BufferedImage texture, int id){
        this.texture = texture;
        this.ID = id;
        
        tiles[id] = this; //Assigns newly created tile ("this") to index corresponding to ID
    }
    
    public void tick(){
        
    }
    
    public void render(Graphics g, int x, int y){
        g.drawImage(texture, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }
    
    /**
     * Returns whether the tile can be walked on or not; default is solid
     * @return Solidity of tile
     */
    public boolean isSolid(){
        return false;
    }
    
    public int getID(){
        return ID;
    }
}
