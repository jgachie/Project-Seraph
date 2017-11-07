/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Skills.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Skills.Skill;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Accelerate extends Skill{
    
    public Accelerate(){
        super("Accelerate",
                "Sariel channels his energy into his legs, increasing his speed and agility to superhuman "
                        + "levels. However, such power is difficult to control, and so with each turn, "
                        + "Sariel runs a risk of fumbling his footwork and injuring himself in the process. "
                        + "This risk reduces as Sariel's Chaos stat increases. (Cost: 15 CP)",
                15,
                50,
                0,
                0,
                0,
                true,
                false,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
    }
    
    @Override
    public void tick() {
        
    }
    
    @Override
    public void render(Graphics g) {
        
    }
    
    @Override
    protected BufferedImage getCurrentAnimationFrame() {
        return null;
    }
    
    @Override
    public void use(PlayableActor user, Actor target, Handler handler){
        super.use(user, target, handler);
        
        UITextBox.resetBAOS();
        System.out.println(user.getName() + " prepares to use the skill...\n");
        
        user.useSkillpoints(pointReq);
        
        Combat.delay();
        
        if (!skillHit(user))
            System.out.println(user.getName() + " done goofed!");
        
        System.out.println("...The skill succeeded!");
        user.modifyStat("Agility", 5);
        System.out.println(user.getName() + "'s agiility increased by 5!");
    }
}