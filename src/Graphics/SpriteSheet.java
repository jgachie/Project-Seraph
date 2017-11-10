/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphics;

import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class SpriteSheet {
    private transient BufferedImage sheet; //BufferedImage of spritesheet
    
    public SpriteSheet(BufferedImage sheet){
        this.sheet = sheet;
    }
    
    /**
     * Crops any sprite sheet into individual sprites
     * @param x Initial x coordinate of sprite
     * @param y Initial y coordinate of sprite
     * @param width Width of sprite
     * @param height Height of sprite
     * @return BufferedImage containing newly cropped sprite
     */
    public BufferedImage crop(int x, int y, int width, int height){
        return sheet.getSubimage(x, y, width, height);
    }
    
    /**
     * Crops creature sprite sheet into individual sprites
     * @param row The row number of the desired sprite
     * @param col The column number of the desired sprite
     * @return BufferedImage containing newly cropped creature sprite
     */
    public BufferedImage cropCreature(int row, int col){
        int width = 16, height = 24; //Default creature width/height
        int space = 4; //Default number of spaces between sprites
        int xStart = col * width + col * space + 4; //Beginning x-coordinate of cropped sprite
        int yStart = row * height + row * space + 4; //Beginning y-coordinate of cropped sprite
        
        return sheet.getSubimage(xStart, yStart, width, height);
    }
}
