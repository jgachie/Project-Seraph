/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Specials.Skills.Basic;

import Entities.Specials.Skills.Skill;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Accelerate extends Skill{
    
    public Accelerate(){
        super("Accelerate",
            "",
            15,
            50,
            0,
            0,
            0,
            true,
            false,
            DEFAULT_SPECIAL_WIDTH,
            DEFAULT_SPECIAL_HEIGHT);
    }

    @Override
    public void tick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Graphics g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected BufferedImage getCurrentAnimationFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void use()
    
}
