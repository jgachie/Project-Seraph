/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Spells.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
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
public class MagicBlast extends Spell{
    private static Animation exploding;
    
    public MagicBlast() {
        super("Magic Blast",
                "Causes a weak explosion with a wide blast radius, inflicting damage on all enemies."
                        + " If the spell fails, damage is inflicted upon the user's party. (Costs 40"
                        + " MP)",
                40,
                50,
                5,
                5,
                10,
                false,
                true,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT);
        
        //Initialize animations
        exploding = new Animation(100, true, Assets.bulletExploding);
    }
    
    @Override
    public void tick() {
        if (!active)
            return;
        
        if (!exploding.isCompleted())
            exploding.tick();
    }
    
    @Override
    public void render(Graphics g) {
        if (!active)
            return;
        
        Enemy enemy = (Enemy) target;
        
        for (Enemy e : enemy.getParty())
            g.drawImage(getCurrentAnimationFrame(), (int) e.getX(), (int) e.getY() + (e.getHeight() / 4), width, height, null);
        
        if (exploding.isCompleted())
            resetValues(exploding);
    }
    
    @Override
    protected BufferedImage getCurrentAnimationFrame(){
        if (!exploding.isCompleted())
            return exploding.getCurrentFrame();
        
        return null; //If all else fails, return null
    }
    
    
    @Override
    public void cast(PlayableActor caster, Actor target, Handler handler) {
        super.cast(caster, target, handler);
        Enemy enemy = (Enemy) target; //Cast the target to an enemy object
        int damageBuffer; //Holds the amount of damage the spell does before factoring in individual resistances
        int[] damages = new int[enemy.getParty().size()]; //An array holding the amount of damage dealt to each individual enemy after factoring in resistances
        
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
        
        //Calculate damage dealt
        damageBuffer = calcDamage(caster);
        
        //Iterate through Enemy party and calculate individual resistances for individual damage values, then deal the damage
        for (int i = 0; i < enemy.getParty().size(); i++){
            Enemy mob = enemy.getParty().get(i);
            
            //Only do damage calculations if the Enemy is still alive
            if (mob.isAlive()){
                damages[i] = mob.calcDamageReceived(caster, damageBuffer, DamageType.MAGIC, StatusEffect.NONE); //Calculate damage after factoring in resistances
                
                //If the spell wasn't evaded or completely blocked, deal the damage to the Enemy; otherwise, do nothing
                if (damages[i] > 0){
                    System.out.println(mob.getName() + " took " + damages[i] + " damage!");
                    mob.dealDamage(damages[i]);
                }
                else
                    System.out.println(mob.getName() + " blocked the spell!");
            }
            
            Combat.delay();
        }
    }
}