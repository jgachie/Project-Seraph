/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Worlds;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActor.PlayableActor;
import Entities.Creatures.Actors.PlayableActor.Player;
import Entities.EntityManager;
import Entities.Statics.Tree;
import Graphics.Assets;
import Main.Handler;
import Tiles.Tile;
import UI.UIManager;
import Utils.Utils;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class World {
    private Handler handler; //The game itself;
    private int width, height; //Width/height in terms of tiles
    private int spawnX, spawnY; //X- and y-coordinates for player spawn point
    private int[][] tiles; //2D array tilemap containing IDs describing world in terms of tiles
    private EntityManager entityManager; //Manager for all entities in the world
    private UIManager uiManager; //Manager for all UI objects in the world
    
    //Combat spawn coordinates for Player and enemy parties; Players always appear on left side of the screen, while enemies appear on the right
    private static final int PARTY_SPAWN_X = 100, ENEMY_SPAWN_X = 700;
    private static final int SPAWN_Y = 100;
    
    
    public World(Handler handler, String path){
        this.handler = handler;
        entityManager = new EntityManager(handler, Player.load(handler)); //Initialize entity manager with new player object
        uiManager = new UIManager(handler);
        
        loadWorld(path); //Load world into game
        
        //Set spawn point of Player
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
    }
    
    public World(Handler handler, String path, ArrayList<PlayableActor> party, ArrayList<Actor> enemyParty){
        this.handler = handler;
        
        uiManager = new UIManager(handler);
        
        loadWorld(path);
        
        //Set Player spawn points
        for (int i = 0; i < party.size(); i++){
            party.get(i).setX(PARTY_SPAWN_X);
            party.get(i).setY(SPAWN_Y + (150 * i));
        }
        
        //Set enemy spawn points
        for (int i = 0; i < enemyParty.size(); i++){
            enemyParty.get(i).setX(ENEMY_SPAWN_X);
            enemyParty.get(i).setY(SPAWN_Y + (150 * i));
        }
    }
    
    public void tick(){
        entityManager.tick();
        uiManager.tick();
    }
    
    public void render(Graphics g){
        /*
        Indices of first and last tiles visible on screen, determined by offsets (divided by tile
        width to get it in terms of tiles. If offset is less than 0, index is 0; if offset is greater
        than width/height, index is that of width/height;
        */
        int xStart = (int) Math.max(0, handler.getGameCamera().getXOffset() / Tile.TILE_WIDTH);
        int xEnd = (int) Math.min(width, (handler.getGameCamera().getXOffset() + handler.getWidth()) / Tile.TILE_WIDTH + 1);
        int yStart = (int) Math.max(0, handler.getGameCamera().getYOffset() / Tile.TILE_HEIGHT);
        int yEnd = (int) Math.min(height, (handler.getGameCamera().getYOffset() + handler.getHeight()) / Tile.TILE_HEIGHT + 1);
        
        //Iterate through all visible tiles using start and end indices as delimiters
        for (int y = yStart; y < yEnd; y++){
            for (int x = xStart; x < xEnd; x++){
                /*
                Render each tile by id; x and y coordinates multiplied by width/height so that units
                are in terms of tiles. X and y coordinates are then subtracted by camera offsets to 
                simulate camera movement.
                */
                getTile(x, y).render(g, (int) (x * Tile.TILE_WIDTH - handler.getGameCamera().getXOffset()),
                        (int) (y * Tile.TILE_HEIGHT - handler.getGameCamera().getYOffset()));
            }
        }
        
        //Render entities and UI objects
        entityManager.render(g);
        uiManager.render(g);
    }
    
    /**
     * Fetches tile by ID given coordinates on tilemap
     * @param x Tilemap x-coordinate of requested tile
     * @param y Tilemap y-coordinate of requested tile
     * @return Requested tile
     */
    public Tile getTile(int x, int y){
        //If x and/or y are invalid coordinates, default to stone tile
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.stoneTile;
        
        Tile t = Tile.tiles[tiles[x][y]]; //Find tile using ID look-up
        
        //If ID hasn't been set yet, default to stone tile
        if (t == null)
            return Tile.stoneTile;
        
        return t; //Return tile
    }
    
    /**
     * Loads world file into game from path
     * <b>Note:</b> First four digits in world file are reserved; first digit represents world width,
     * second represents world height, third is player spawn x-coordinate, and fourth is player spawn
     * y-coordinate. All digits afterward are IDs for tiles to build the world.
     * @param path File path of world file
     */
    private void loadWorld(String path){
        String file = Utils.loadFileAsString(path); //Load up world file as String object
        String[] tokens = file.split("\\s+"); //String tokenizer; separates content of "file" into smaller Strings by spaces
        width = Utils.parseInt(tokens[0]); //Convert String within first token into integer and use it to set width
        height = Utils.parseInt(tokens[1]); //Do the same for second token and height;
        spawnX = Utils.parseInt(tokens[2]); //Now for the Player's spawn x-coordinate
        spawnY = Utils.parseInt(tokens[3]); //And finally the spawn y-coordinate
        
        tiles = new int[width][height]; //Initialize tilemap
        
        //Iterate through entire tilemap
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                tiles[x][y] = Utils.parseInt(tokens[(x + y * width) + 4]); //Set each tilemap index to corresponding token; token index is determined by multiplying y index by width (to denote row placement) and adding x index, plus 4 (to account for four reserved values)
            }
        }
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    public UIManager getUIManager(){
        return uiManager;
    }
}