/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Spells.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.DamageType;
import Enums.StatusEffect;
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
public class MagicBullet extends Spell{
    //Declare animations (must be static for serialization)
    private static Animation forming, exploding;
    
    public MagicBullet() {
        super("Magic Bullet",
                "Hurls a small, concentrated mass of magical energy at a single target, inflicting damage."
                        + " (Costs 20 MP)",
                20,
                65,
                10,
                8,
                13,
                false,
                false,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
        
        //Set bounding box coordinates (relative to top-left corner of entity) and width/height
        bounds.x = 0;
        bounds.y = 0;
        bounds.width = DEFAULT_WIDTH;
        bounds.height = DEFAULT_HEIGHT;
        
        //Initialize animations
        forming = new Animation(100, true, Assets.bulletForming);
        exploding = new Animation(100, true, Assets.bulletExploding);
    }
    
    @Override
    public void tick(){
        //If the spell isn't active (isn't being cast right now), do nothing and return
        if (!active)
            return;
        
        //If the forming animation is still running, tick the animation
        if (!forming.isCompleted())
            forming.tick();
        //If the forming animation has completed but the spell hasn't hit yet, move it toward the target
        else if (forming.isCompleted() && !hit){
            yMove = calcVerticalMovement();
            move();
            
            if (x + width == target.getX())
                hit = true;
        }
        //If the forming animation has completed and the spell has hit, tick the exploding animation
        else if (forming.isCompleted() && hit && !exploding.isCompleted())
            exploding.tick();
    }
    
    /**
     * Calculates the vertical movement relative to the horizontal and vertical distances between the
     * spell and the target
     * @return
     */
    private float calcVerticalMovement(){
        float xDistance, yDistance; //The horizontal and vertical distances between the target and the spell
        
        xDistance = target.getX() - (x + width); //Calculate horizontal distance by subtracting x-coordinate at front of spell from target's x-coordinate
        yDistance = target.getY() + (target.getHeight() / 4)  - y; //Calcaulate vertical distance by subracting spell's y-coordinate from target's y-coordinate
        
        return yDistance / xDistance; //The amount of vertical movement should be equal to the vertical distance divided by the horizontal distance
    }
    
    @Override
    public void render(Graphics g){
        //If the spell isn't active (isn't being cast right now), do nothing and return
        if (!active)
            return;
        
        g.drawImage(getCurrentAnimationFrame(), (int) (x), (int) (y), width, height, null);
        
        //If all animations have completed, reset all values
        if (forming.isCompleted() && hit && exploding.isCompleted())
            resetValues(forming, exploding);
    }
    
    @Override
    protected BufferedImage getCurrentAnimationFrame(){
        //If the forming animation is still running, return the current frame
        if (!forming.isCompleted())
            return forming.getCurrentFrame();
        //If the forming animation has completed but the spell hasn't hit yet, return the static image asset
        else if (forming.isCompleted() && !hit)
            return Assets.magicBullet;
        //If the forming animation has completed and the spell has hit, return the current frame of the exploding animation
        else if (forming.isCompleted() && hit && !exploding.isCompleted()){
            //Reposition the bullet so that it explodes on the enemy instead of just in front of it
            x = target.getX();
            y = target.getY() + (target.getHeight() / 4);
            return exploding.getCurrentFrame();
        }
        
        return null; //If all else fails, return null
    }
    
    @Override
    public void cast(PlayableActor caster, Actor target, Handler handler) {
        super.cast(caster, target, handler);
        x = caster.getX() + caster.getWidth(); //Initialize x position of the spell to just in front of the caster
        y = caster.getY() + (caster.getHeight() / 4); //Initialize y position of the spell to just in front of the caster
        targetX = target.getX(); //Initialize x position of the spell's target
        targetY = target.getY(); //Initialize y position of the spell's target
        
        int damage;
        
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
        
        //Calculate the damage dealt
        damage = calcDamage(caster);
        
        //Factor in enemy resistances
        damage = target.calcDamageReceived(caster, damage, DamageType.MAGIC, StatusEffect.NONE);
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0){
            System.out.println(target.getName() + " took " + damage + " damage");
            target.dealDamage(damage);
        }
        else
            System.out.println(target.getName() + " blocked the spell!");
    }
}