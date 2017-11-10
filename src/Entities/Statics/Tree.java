/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Statics;

import Graphics.Assets;
import Main.Handler;
import Tiles.Tile;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public class Tree extends StaticEntity{
    
    public Tree(Handler handler, float x, float y){
        super(handler, x, y, Tile.TILE_WIDTH *2, Tile.TILE_HEIGHT * 4);
        
        bounds.x = 24;
        bounds.y = 80;
        bounds.width = 12;
        bounds.height = 48;
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.tree, (int) (x - handler.getGameCamera().getXOffset()), (int) (y - handler.getGameCamera().getYOffset()), width, height, null);
    }
}