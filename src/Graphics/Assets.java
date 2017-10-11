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
public class Assets {
    private static final int C_WIDTH = 16, C_HEIGHT = 24; //Creature default width/height
    private static final int T_WIDTH = 32, T_HEIGHT = 32; //Tile default width/height
    
    //Image assets
    public static BufferedImage player, goblin, grass, water, stone, dirt, tree;
    
    //Animation assets
    public static BufferedImage[] player_down, player_up, player_right, player_left;
    public static BufferedImage[] goblin_down, goblin_up, goblin_right, goblin_left;
    public static BufferedImage[] btn_start;
    
    /**
     * Initialize all assets
     */
    public static void init(){
        //Load sprite sheets
        SpriteSheet seraph = new SpriteSheet(ImageLoader.loadImage("/Textures/Seraph/Seraph Spritesheet.png"));
        SpriteSheet goblinSheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Goblin/Goblin Spritesheet.png"));
        SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Spritesheet.png"));
        
        //Crop static sprites
        player = seraph.cropCreature(0, 0);
        goblin = goblinSheet.cropCreature(0, 0);
        grass = sheet.crop(T_WIDTH, 0, T_WIDTH, T_HEIGHT);
        water = sheet.crop(T_WIDTH * 2, 0, T_WIDTH, T_HEIGHT);
        stone = sheet.crop(T_WIDTH * 3, 0, T_WIDTH, T_HEIGHT);
        dirt = sheet.crop(T_WIDTH * 4, 0, T_WIDTH, T_HEIGHT);
        tree = sheet.crop(T_WIDTH * 5, 0, T_WIDTH, T_HEIGHT * 2);
        
        //Initialize animations
        player_down = new BufferedImage[2];
        player_up = new BufferedImage[2];
        player_right = new BufferedImage[4];
        player_left = new BufferedImage[4];
        
        goblin_down = new BufferedImage[2];
        goblin_up = new BufferedImage[2];
        goblin_right = new BufferedImage[4];
        goblin_left = new BufferedImage[4];
        
        btn_start = new BufferedImage[2];
        
        //Crop animations
        player_down[0] = seraph.cropCreature(0, 1);
        player_down[1] = seraph.cropCreature(0, 2);
        
        player_up[0] = seraph.cropCreature(1, 1);
        player_up[1] = seraph.cropCreature(1, 2);
        
        player_right[0] = seraph.cropCreature(2, 1);
        player_right[1] = seraph.cropCreature(2, 0);
        player_right[2] = seraph.cropCreature(2, 2);
        player_right[3] = seraph.cropCreature(2, 0);
        
        player_left[0] = seraph.cropCreature(3, 1);
        player_left[1] = seraph.cropCreature(3, 0);
        player_left[2] = seraph.cropCreature(3, 2);
        player_left[3] = seraph.cropCreature(3, 0);
        
        goblin_down[0] = goblinSheet.cropCreature(0, 1);
        goblin_down[1] = goblinSheet.cropCreature(0, 2);
        
        goblin_up[0] = goblinSheet.cropCreature(1, 1);
        goblin_up[1] = goblinSheet.cropCreature(1, 2);
        
        goblin_right[0] = goblinSheet.cropCreature(2, 1);
        goblin_right[1] = goblinSheet.cropCreature(2, 0);
        goblin_right[2] = goblinSheet.cropCreature(2, 2);
        goblin_right[3] = goblinSheet.cropCreature(2, 0);
        
        goblin_left[0] = goblinSheet.cropCreature(3, 1);
        goblin_left[1] = goblinSheet.cropCreature(3, 0);
        goblin_left[2] = goblinSheet.cropCreature(3, 2);
        goblin_left[3] = goblinSheet.cropCreature(3, 0);
        
        btn_start[0] = sheet.crop(T_WIDTH * 10, 0, T_WIDTH * 4, T_HEIGHT * 2);
        btn_start[1] = sheet.crop(T_WIDTH * 6, 0, T_WIDTH * 4, T_HEIGHT * 2);
    }
}
