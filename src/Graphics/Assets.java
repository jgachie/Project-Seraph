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
    public transient static BufferedImage player, goblin, grass, water, stone, dirt, tree, btn, textBox, pauseScreen, pointer, magicBullet;
    
    //Animation assets
    public transient static BufferedImage[] playerDown, playerUp, playerRight, playerLeft;
    public transient static BufferedImage[] playerRunDown, playerRunUp, playerRunRight, playerRunLeft;
    public transient static BufferedImage[] playerFightDown, playerFightUp, playerFightLeft, playerFightRight;
    public transient static BufferedImage[] playerAttackRight, playerAttackLeft;
    public transient static BufferedImage[] playerCastRight, playerCastLeft;
    public transient static BufferedImage[] goblinDown, goblinUp, goblinRight, goblinLeft;
    public transient static BufferedImage[] goblinFightDown, goblinFightUp, goblinFightLeft, goblinFightRight;
    public transient static BufferedImage[] goblinAttackRight, goblinAttackLeft;
    public transient static BufferedImage[] bulletForming, bulletExploding;
    public transient static BufferedImage[] refresh;
    
    /**
     * Initialize all assets
     */
    public static void init(){
        //Load sprite sheets
        SpriteSheet seraphSheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Seraph/Seraph Spritesheet.png"));
        SpriteSheet goblinSheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Goblin/Goblin Spritesheet.png"));
        SpriteSheet effectSheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Effects/Effects Spritesheet.png"));
        SpriteSheet miscSheet = new SpriteSheet(ImageLoader.loadImage("/Textures/Spritesheet.png"));
        
        //Crop static sprites
        player = seraphSheet.cropCreature(0, 0);
        goblin = goblinSheet.cropCreature(0, 0);
        grass = miscSheet.crop(T_WIDTH, 0, T_WIDTH, T_HEIGHT);
        water = miscSheet.crop(T_WIDTH * 2, 0, T_WIDTH, T_HEIGHT);
        stone = miscSheet.crop(T_WIDTH * 3, 0, T_WIDTH, T_HEIGHT);
        dirt = miscSheet.crop(T_WIDTH * 4, 0, T_WIDTH, T_HEIGHT);
        tree = miscSheet.crop(T_WIDTH * 5, 0, T_WIDTH, T_HEIGHT * 2);
        btn = miscSheet.crop(192, 0, T_WIDTH * 4, T_HEIGHT * 2);
        textBox = miscSheet.crop(0, 64, 1024, 300);
        pauseScreen = miscSheet.crop(0, 364, 1024, 768);
        pointer = miscSheet.crop(320, 0, 38, 18);
        magicBullet = effectSheet.crop(84, 37, 16, 16);
        
        //Initialize animations
        playerDown = new BufferedImage[2];
        playerUp = new BufferedImage[2];
        playerRight = new BufferedImage[4];
        playerLeft = new BufferedImage[4];
        
        playerFightDown = new BufferedImage[2];
        playerFightUp = new BufferedImage[2];
        playerFightRight = new BufferedImage[2];
        playerFightLeft = new BufferedImage[2];
        
        playerAttackRight = new BufferedImage[5];
        playerAttackLeft = new BufferedImage[5];
        playerCastRight = new BufferedImage[4];
        playerCastLeft = new BufferedImage[4];
        
        goblinDown = new BufferedImage[2];
        goblinUp = new BufferedImage[2];
        goblinRight = new BufferedImage[4];
        goblinLeft = new BufferedImage[4];
        
        goblinFightDown = new BufferedImage[2];
        goblinFightUp = new BufferedImage[2];
        goblinFightRight = new BufferedImage[2];
        goblinFightLeft = new BufferedImage[2];
        
        goblinAttackRight = new BufferedImage[5];
        goblinAttackLeft = new BufferedImage[5];
        
        bulletForming = new BufferedImage[4];
        bulletExploding = new BufferedImage[5];
        
        refresh = new BufferedImage[8];
        
        //Crop animations
        
        //Player
        playerDown[0] = seraphSheet.cropCreature(0, 1);
        playerDown[1] = seraphSheet.cropCreature(0, 2);
        
        playerUp[0] = seraphSheet.cropCreature(1, 1);
        playerUp[1] = seraphSheet.cropCreature(1, 2);
        
        playerRight[0] = seraphSheet.cropCreature(2, 1);
        playerRight[1] = seraphSheet.cropCreature(2, 0);
        playerRight[2] = seraphSheet.cropCreature(2, 2);
        playerRight[3] = seraphSheet.cropCreature(2, 0);
        
        playerLeft[0] = seraphSheet.cropCreature(3, 1);
        playerLeft[1] = seraphSheet.cropCreature(3, 0);
        playerLeft[2] = seraphSheet.cropCreature(3, 2);
        playerLeft[3] = seraphSheet.cropCreature(3, 0);
        
        playerFightDown[0] = seraphSheet.cropCreature(0, 5);
        playerFightDown[1] = seraphSheet.cropCreature(0, 6);
        
        playerFightUp[0] = seraphSheet.cropCreature(1, 5);
        playerFightUp[1] = seraphSheet.cropCreature(1, 6);
        
        playerFightRight[0] = seraphSheet.cropCreature(2, 5);
        playerFightRight[1] = seraphSheet.cropCreature(2, 6);
        
        playerFightLeft[0] = seraphSheet.cropCreature(3, 5);
        playerFightLeft[1] = seraphSheet.cropCreature(3, 6);
        
        playerAttackRight[0] = seraphSheet.crop(4, 116, C_WIDTH, C_HEIGHT);
        playerAttackRight[1] = seraphSheet.crop(24, 116, 22, C_HEIGHT);
        playerAttackRight[2] = seraphSheet.crop(50, 116, 26, C_HEIGHT);
        playerAttackRight[3] = seraphSheet.crop(80, 116, 29, C_HEIGHT);
        playerAttackRight[4] = seraphSheet.crop(113, 116, 31, C_HEIGHT);
        
        playerAttackLeft[0] = seraphSheet.crop(4, 146, C_WIDTH, C_HEIGHT);
        playerAttackLeft[1] = seraphSheet.crop(24, 146, 22, C_HEIGHT);
        playerAttackLeft[2] = seraphSheet.crop(50, 146, 26, C_HEIGHT);
        playerAttackLeft[3] = seraphSheet.crop(80, 146, 29, C_HEIGHT);
        playerAttackLeft[4] = seraphSheet.crop(113, 146, 31, C_HEIGHT);
        
        
        playerCastRight[0] = seraphSheet.cropCreature(6, 0);
        playerCastRight[1] = seraphSheet.cropCreature(6, 1);
        playerCastRight[2] = seraphSheet.cropCreature(6, 2);
        playerCastRight[3] = seraphSheet.cropCreature(6, 3);
        
        playerCastLeft[0] = seraphSheet.cropCreature(7, 0);
        playerCastLeft[1] = seraphSheet.cropCreature(7, 1);
        playerCastLeft[2] = seraphSheet.cropCreature(7, 2);
        playerCastLeft[3] = seraphSheet.cropCreature(7, 3);
        
        //Goblin
        goblinDown[0] = goblinSheet.cropCreature(0, 1);
        goblinDown[1] = goblinSheet.cropCreature(0, 2);
        
        goblinUp[0] = goblinSheet.cropCreature(1, 1);
        goblinUp[1] = goblinSheet.cropCreature(1, 2);
        
        goblinRight[0] = goblinSheet.cropCreature(2, 1);
        goblinRight[1] = goblinSheet.cropCreature(2, 0);
        goblinRight[2] = goblinSheet.cropCreature(2, 2);
        goblinRight[3] = goblinSheet.cropCreature(2, 0);
        
        goblinLeft[0] = goblinSheet.cropCreature(3, 1);
        goblinLeft[1] = goblinSheet.cropCreature(3, 0);
        goblinLeft[2] = goblinSheet.cropCreature(3, 2);
        goblinLeft[3] = goblinSheet.cropCreature(3, 0);
        
        goblinFightDown[0] = goblinSheet.cropCreature(0, 5);
        goblinFightDown[1] = goblinSheet.cropCreature(0, 6);
        
        goblinFightUp[0] = goblinSheet.cropCreature(1, 5);
        goblinFightUp[1] = goblinSheet.cropCreature(1, 6);
        
        goblinFightRight[0] = goblinSheet.cropCreature(2, 5);
        goblinFightRight[1] = goblinSheet.cropCreature(2, 6);
        
        goblinFightLeft[0] = goblinSheet.cropCreature(3, 5);
        goblinFightLeft[1] = goblinSheet.cropCreature(3, 6);
        
        goblinAttackLeft[0] = goblinSheet.cropCreature(4, 0);
        goblinAttackLeft[1] = goblinSheet.crop(24, 116, 21, C_HEIGHT);
        goblinAttackLeft[2] = goblinSheet.crop(49, 116, 26, C_HEIGHT);
        goblinAttackLeft[3] = goblinSheet.crop(79, 116, 31, C_HEIGHT);
        goblinAttackLeft[4] = goblinSheet.crop(114, 116, 31, C_HEIGHT);
        
        //Magic Bullet
        bulletForming[0] = effectSheet.crop(4, 37, 16, 16);
        bulletForming[1] = effectSheet.crop(24, 37, 16, 16);
        bulletForming[2] = effectSheet.crop(44, 37, 16, 16);
        bulletForming[3] = effectSheet.crop(64, 37, 16, 16);
        
        bulletExploding[0] = effectSheet.crop(104, 37, 16, 16);
        bulletExploding[1] = effectSheet.crop(124, 37, 16, 16);
        bulletExploding[2] = effectSheet.crop(144, 37, 16, 16);
        bulletExploding[3] = effectSheet.crop(164, 37, 16, 16);
        bulletExploding[4] = effectSheet.crop(184, 37, 16, 16);
        
        //Refresh
        refresh[0] = effectSheet.crop(4, 4, C_WIDTH, C_HEIGHT);
        refresh[1] = effectSheet.crop(24, 4, C_WIDTH, C_HEIGHT);
        refresh[2] = effectSheet.crop(44, 4, C_WIDTH, C_HEIGHT);
        refresh[3] = effectSheet.crop(64, 4, C_WIDTH, C_HEIGHT);
        refresh[4] = refresh[0];
        refresh[5] = refresh[1];
        refresh[6] = refresh[2];
        refresh[7] = refresh[3];
    }
}
