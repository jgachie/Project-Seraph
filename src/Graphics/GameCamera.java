/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import Entities.Entity;
import Main.Handler;
import Tiles.Tile;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public class GameCamera implements Serializable{
    private Handler handler;
    private float xOffset, yOffset; //Offset variables; how far off from original position (relative to original camera angle) everything is drawn
    
    public GameCamera(Handler handler, float xOffset, float yOffset){
        this.handler = handler;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    /**
     * Checks to see if there is any blank space on the screen; if there is, the camera moves to
     * eliminate it
     */
    public void checkBlankSpace(){
        //If the camera is showing space before/after the first/last tile (horizontally), refix the camera on the first/last tile
        if (xOffset < 0)
            xOffset = 0;
        else if (xOffset > handler.getWorld().getWidth() * Tile.TILE_WIDTH - handler.getWidth())
            xOffset = handler.getWorld().getWidth() * Tile.TILE_WIDTH - handler.getWidth();
        
        //If the camera is showing space before/after the first/last tile (vertically), refix the camera on the first/last tile
        if (yOffset < 0)
            yOffset = 0;
        else if (yOffset > handler.getWorld().getHeight() * Tile.TILE_HEIGHT - handler.getHeight())
            yOffset = handler.getWorld().getHeight() * Tile.TILE_HEIGHT - handler.getHeight();
    }
    
    /**
     * Centers camera on given entity so that it follows the entity across the world
     * @param e The entity the camera is being centered on
     */
    public void centerOnEntity(Entity e){
        /*
        Set offsets to player's position minus half of the sreen width/height so the player appears
        in the center of the screen. Add half of entity width and height to focus on player's center
        instead of top-left corner.
        */
        xOffset = e.getX() - handler.getWidth() / 2 + e.getWidth() / 2;
        yOffset = e.getY() - handler.getHeight() / 2 + e.getHeight() / 2;
        
        checkBlankSpace(); //Check for and elminate blank space
    }
    
    /**
     * Game camera's move method; amount moved is added to offset variables to reposition camera
     * @param xAmt The amount by which the camera has been moved horizontally
     * @param yAmt The amount by which the camera has been moved vertically
     */
    public void move(float xAmt, float yAmt){
        xOffset += xAmt;
        yOffset += yAmt;
        
        checkBlankSpace(); //Check for and elminate blank space
    }

    public float getXOffset() {
        return xOffset;
    }

    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getYOffset() {
        return yOffset;
    }

    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }
    
}
