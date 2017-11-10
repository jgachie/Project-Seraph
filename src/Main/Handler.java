/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Combat.Combat;
import Combat.Encounter;
import Graphics.GameCamera;
import Input.KeyManager;
import Input.MouseManager;
import Worlds.World;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public class Handler implements Serializable{
    private Game game; //The game itself
    private World world; //The world
    private transient Combat combat; //The thread that combat is running on
    private Encounter encounter; //The next encounter
    
    public Handler(Game game){
        this.game = game;
    }
    
    public GameCamera getGameCamera(){
        return game.getGameCamera();
    }
    
    public KeyManager getKeyManager(){
        return game.getKeyManager();
    }
    
    public MouseManager getMouseManager(){
        return game.getMouseManager();
    }
    
    public int getWidth(){
        return game.getWidth();
    }
    
    public int getHeight(){
        return game.getHeight();
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void setWorld(World world) {
        this.world = world;
    }
    
    public Combat getCombat(){
        return combat;
    }
    
    public void setCombat(Combat combat){
        this.combat = combat;
    }
    
    public Encounter getEncounter() {
        return encounter;
    }
    
    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }
}
