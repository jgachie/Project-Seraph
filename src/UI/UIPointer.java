/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Graphics.Assets;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class UIPointer extends UIObject{
    private static BufferedImage img = Assets.pointer;

    public UIPointer(float x, float y, int width, int height, boolean visible){
        super(x, y, width, height, visible);
    }

    @Override
    public void tick() {
        
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, (int) x, (int) y, width, height, null);
    }

    @Override
    public void onClick() {
    }
}