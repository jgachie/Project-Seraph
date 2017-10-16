/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Worlds;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Creatures.Actors.PlayableActors.Player;
import Entities.EntityManager;
import Graphics.Assets;
import Main.Handler;
import Tiles.Tile;
import UI.UIImageButton;
import UI.UIManager;
import Utils.Utils;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class World {
    private Handler handler; //The game itself
    private Combat combat; //The combat object
    private int width, height; //Width/height in terms of tiles
    private int spawnX, spawnY; //X- and y-coordinates for player spawn point
    private int[][] tiles; //2D array tilemap containing IDs describing world in terms of tiles
    private EntityManager entityManager; //Manager for all entities in the world
    private UIManager uiManager; //Manager for all UI objects in the world
    private UIImageButton attackButton, spellButton, skillButton, itemButton, fleeButton, backButton; //Action buttons
    
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
    
    public World(Handler handler, String path, ArrayList<PlayableActor> party, ArrayList<Actor> enemyParty, Combat combat){
        this.handler = handler;
        this.combat = combat;
        
        entityManager = new EntityManager(handler); //Initialize entity manager
        
        //Iterate through Player's party and add all members to entity manager
        for (PlayableActor member : party)
            entityManager.addEntity(member);
        
        //Do the same for enemy party
        for (Actor member : enemyParty)
            entityManager.addEntity(member);
        
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager); //Feed the manager through to the Mouse Manager to facilitate click and move events
        
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
        
        /*
        Add back button as its own button within each button's click method
        
        Figure out how to remove temporary buttons from UI Manager after they've been clicked
        */
        
        //Set click action of attack button
        attackButton = new UIImageButton("Attack", 50,  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
            //If the combat thread isn't ready yet, do nothing and return
            if (!combat.isReady())
                return;
            
            PlayableActor actor = combat.getTurn(); //Get the Actor whose turn it is currently
            hideButtons(); //Hide the combat buttons
            
            //Iterate through the enemy's party and create a button that refers to each one so the Player can select a target
            for (int i = 0; i < enemyParty.size(); i++){
                Actor enemy = enemyParty.get(i); //The enemy mapped to this button
                
                //If the enemy is alive, create a button for it so it can be targeted; if not, do nothing
                if (enemy.isAlive()){
                    uiManager.addObject(new UIImageButton(enemy.getName(), 50 + (150 * i),  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
                        //Load Actor's action buffer with method call
                        actor.setAction(() -> {
                            actor.attack(enemy); //Attack
                        });
                        showButtons(); //Show the combat buttons again for the next Actor's turn
                        synchronized(combat){
                            combat.notifyAll(); //Let the combat thread know to move on
                        }
                        uiManager.removeLast(); //Remove the object from the UI Manager
                    }));
                }
            }
        });
        
        //Set click action of spell button
        spellButton = new UIImageButton("Cast Spell", 200,  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
            //If the combat thread isn't ready yet, do nothing and return
            if (!combat.isReady())
                return;
            
            PlayableActor actor = (PlayableActor) combat.getTurn(); //Get the Actor whose turn it is currently
            String[] spellNames = actor.getGrimoire().getSpellNames(); //The names of the spells in the Actor's currently equipped grimoire
            hideButtons(); //Hide the combat buttons
            
            //Iterate through the Actor's Grimoire's spell names to create a button that refers to each one so the Player can select a spell
            for (int i = 0; i < spellNames.length; i++){
                final int index = i; //Necessary because Java is fucking ridiculous; essentially replaces "i" in the loop
                
                uiManager.addObject(new UIImageButton(spellNames[index], 50 + (150 * index), handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
                    //Iterate through the enemy's party and create a button that refers to each one so the Player can select a target
                    for (int j = 0; j < enemyParty.size(); j++){
                        Actor enemy = enemyParty.get(j); //The enemy mapped to this button
                        
                        //If the enemy is alive, create a button for it so it can be targeted; if not, do nothing
                        if (enemy.isAlive()){
                            uiManager.addObject(new UIImageButton(enemy.getName(), 50 + (150 * index),  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
                                //Load Actor's action buffer with method call
                                actor.setAction(() -> {
                                    actor.castSpell(enemy, index); //Cast spell
                                });
                                showButtons(); //Show the combat buttons again for the next Actor's turn
                                synchronized(combat){
                                    combat.notifyAll(); //Let the combat thread know to move on
                                }
                                uiManager.removeLast(); //Remove the object from the UI Manager
                            }));
                        }
                    }
                    uiManager.removeLast(); //Remove the object from the UI Manager
                }));
            }
            
        });
        
        //Set click action of skill button
        skillButton = new UIImageButton("Use Skill", 350,  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
            //If the combat thread isn't ready yet, do nothing and return
            if (!combat.isReady())
                return;
            
            PlayableActor actor = combat.getTurn(); //Get the actor whose turn it is currently
            String[] skillNames = actor.getTome().getSkillNames(); //The names of the skills in the Actor's currently equipped tome
            hideButtons(); //Hide the combat buttons
            
            //Iterate through the Actor's Tome's skill names to create a button that refers to each one so the Player can select a skill
            for (int i = 0; i < skillNames.length; i++){
                final int index = i; //Necessary because Java is fucking ridiculous; essentially replaces "i" in the loop
                uiManager.addObject(new UIImageButton(skillNames[index], 50 + (150 * index), handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
                    //Iterate through the enemy's party and create a button that refers to each one so the Player can select a target
                    for (int j = 0; j < enemyParty.size(); j++){
                        Actor enemy = enemyParty.get(j); //The enemy mapped to this button
                        
                        //If the enemy is alive, create a button for it so it can be targeted; if not, do nothing
                        if (enemy.isAlive()){
                            uiManager.addObject(new UIImageButton(enemy.getName(), 50 + (150 * index), handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
                                //Load Actor's action buffer with method call
                                actor.setAction(() -> {
                                    actor.castSpell(enemy, index); //Cast spell
                                });
                                showButtons(); //Show the combat buttons again for the next Actor's turn
                                synchronized(combat){
                                    combat.notifyAll(); //Let the combat thread know to move on
                                }
                                uiManager.removeLast(); //Remove the object from the UI Manager
                            }));
                        }
                    }
                    uiManager.removeLast(); //Remove the object from the UI Manager
                }));
            }
        });
        
        //Set click action of item button
        itemButton = new UIImageButton("Use Item", 500,  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
            //If the combat thread isn't ready yet, do nothing and return
            if (!combat.isReady())
                return;
            
            //Implement this later
        });
        
        //Set click action of flee button
        fleeButton = new UIImageButton("Flee", 650,  handler.getHeight() - 150, 128, 64, true, Assets.btn_start, () -> {
            //If the combat thread isn't ready yet, do nothing and return
            if (!combat.isReady())
                return;
            
            //Also implement this later...
        });
        
        uiManager.addObject(attackButton);
        uiManager.addObject(spellButton);
        uiManager.addObject(skillButton);
        uiManager.addObject(itemButton);
        uiManager.addObject(fleeButton);
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
    
    /**
     * Shows all of the main combat buttons visible
     */
    public void showButtons(){
        attackButton.setVisible(true);
        spellButton.setVisible(true);
        skillButton.setVisible(true);
        itemButton.setVisible(true);
        fleeButton.setVisible(true);
    }
    
    /**
     * Hides all of the main combat buttons
     */
    public void hideButtons(){
        attackButton.setVisible(false);
        spellButton.setVisible(false);
        skillButton.setVisible(false);
        itemButton.setVisible(false);
        fleeButton.setVisible(false);
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