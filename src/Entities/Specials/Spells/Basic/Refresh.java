/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Spells.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Spells.Spell;
import Graphics.Animation;
import Graphics.Assets;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Refresh extends Spell{
    private static Animation healing;
    
    public Refresh() {
        super("Refresh",
                "A low-level healing spell that leaves a single target feeling rested and ready for "
                        + "battle once more. (Costs 35 MP)",
                35,
                60,
                10,
                5,
                10,
                true,
                false,
                DEFAULT_WIDTH,
                48);
        
        //Initialize animations
        healing = new Animation(150, true, Assets.refresh);
    }
    
    @Override
    public void tick() {
        if (!active)
            return;
        
        if (!healing.isCompleted())
            healing.tick();
    }
    
    @Override
    public void render(Graphics g) {
        if (!active)
            return;
        
        
        g.drawImage(getCurrentAnimationFrame(), (int) (x), (int) (y), width, height, null);
        
        //If all animations have completed, reset all values
        if (healing.isCompleted())
            resetValues(healing);
    }
    
    @Override
    protected BufferedImage getCurrentAnimationFrame(){
        if (!healing.isCompleted())
            return healing.getCurrentFrame();
        
        return null; //If all else fails, return null
    }
    
    @Override
    public void cast(PlayableActor caster, Actor target, Handler handler) {
        super.cast(caster, target, handler);
        x = target.getX();
        y = target.getY();
        
        int intelligence = caster.getIntelligence(); //The caster's intelligence
        int restore = 0; //The amount of hitpoints restored by the spell
        int baseRestore = dieRoll.nextInt(maxDamage + 1) + minDamage;
        
        UITextBox.resetBAOS();
        System.out.println(caster.getName() + " prepares to cast the spell...\n");
        
        caster.setCasting(true);
        
        Combat.delay();
        
        caster.useMana(pointReq); //Subtract the spell's required mana from the caster's current mana
        
        //If the spell misses, output a failure message and return
        if (!spellHit(caster)){
            System.out.println("...The spell failed!");
            caster.setCasting(false);
            return;
        }
        
        active = true; //Set active to true so that the animation can run
        handler.getWorld().getEntityManager().addEntity(this); //Add the spell to the entity manager so it can be ticked and rendered
        handler.getCombat().animationDelay(); //Wait for the animation to complete
        caster.setCasting(false);
        caster.resetAnimations();
        
        System.out.println("...The spell succeeded!");
        
        //Calculate number of hitpoints to be restored
        if (intelligence < 25)
            restore = (int) (baseRestore * (intelligence / 5.0));
        else if (25 <= intelligence && intelligence < 50)
            restore = (int) (baseRestore * (intelligence / 10.0) + (baseRestore * 2.4));
        else if (intelligence > 50)
            restore = (int) (baseRestore * (intelligence / 20.0) + (baseRestore * 4.9));
        
        //If the spell was critical, multiply the number of hitpoints restored by 2
        if (spellCrit(caster)){
            System.out.println("It was a critical hit!");
            restore *= 2;
        }
        
        System.out.println(target.getName() + " recovered " + restore + " hitpoints!");
        target.heal(restore); //Heal the target
    }
}