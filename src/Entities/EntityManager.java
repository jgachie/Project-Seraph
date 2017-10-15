/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities;

import Entities.Creatures.Actors.PlayableActor.Player;
import Main.Handler;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author Soup
 */
public class EntityManager {
    private Handler handler; //Handler
    private Player player; //The player
    private ArrayList<Entity> entities; //ArrayList holding all of the entities in the game
    //Comparator to sort the entity list by bottom y-coordinate (lowest to highest) for render order
    private Comparator<Entity> renderSorter = new Comparator<Entity>(){
        /**
         * Compares Entities a and b to see which should render first
         */
        @Override
        public int compare(Entity a, Entity b) {
            //If the bottom of Entity a is higher up on the screen than that of Entity b, render a first; if not, render b first
            if (a.getY() + a.getHeight() < b.getY() + b.getHeight())
                return -1;
            else
                return 1;
        }
        
    };
    
    public EntityManager(Handler handler, Player player){
        this.handler = handler;
        this.player = player;
        entities = new ArrayList<Entity>(); //Initialize entity list
        addEntity(player); //Add player to entity list
    }
    
    public void tick(){
        //Iterates through entity list and ticks each entity (DON'T CHANGE THIS; NOT AN ENHANCED FOR LOOP FOR A REASON)
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()){
            Entity e = it.next();
            e.tick();
            if (!e.isActive())
                it.remove();
        }
        
        entities.sort(renderSorter); //Sort entity list for proper rendering order
    }
    
    public void render(Graphics g){
        //Iterates through entity list and renders each entity
        for (Entity e : entities)
            e.render(g);
    }
    
    /**
     * Adds an entity to the entity list
     * @param e The entity to be added
     */
    public void addEntity(Entity e){
        entities.add(e);
    }
    
    public Handler getHandler() {
        return handler;
    }
    
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }
}
