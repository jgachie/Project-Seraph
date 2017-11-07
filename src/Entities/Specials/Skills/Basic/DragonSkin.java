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
import Enums.StatusEffect;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class DragonSkin extends Skill{
    
    public DragonSkin(){
        super("Dragon Skin",
                "Sariel condenses the chaos energy emanating from his body into a rough, protective "
                        + "exoskeleton that lasts for three turns. In addition to raising Sariel's defense "
                        + "stat, the exoskeleton deals a small amount of damage to any enemy that launches"
                        + "a physical attack against him. However, while this skill is active, Sariel "
                        + "will be unaffected by any restorative items or spells. Recoil damage scales "
                        + "with the chaos stat. (Cost: 50 CP)",
                50,
                100,
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
        
        System.out.println("...The skill succeeded!");
        user.addTempEffect(StatusEffect.DRAGON_SKIN, 3);
        System.out.println(user.getName() + "'s skin hardens into scales and a dark haze envelopes his body...");
        user.modifyStat("Defense", 3);
        System.out.println(user.getName() + "'s defense increased by 3!");
    }
}