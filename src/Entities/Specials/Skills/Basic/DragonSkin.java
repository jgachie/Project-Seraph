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
import Enums.DamageType;
import Enums.Stat;
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
        user.addEffect(StatusEffect.DRAGON_SKIN, 3);
        System.out.println(user.getName() + "'s skin hardens into scales and a dark haze envelopes his body...");
        user.modifyStat(Stat.DEFENSE, 3);
        System.out.println(user.getName() + "'s Defense increased by 3!");
    }
    
    /**
     * Deals the recoil damage from being attacked while Dragon Skin is active
     * @param actor The PlayableActor dealing damage
     * @param target The Actor receiving damage
     */
    public static void dragonDamage(PlayableActor actor, Actor target){
        double damageModifier = (.5 * dieRoll.nextDouble()); //Calculate damage modifier (should be from 0 to .5)
        int damage = (int) (actor.getSkill() * damageModifier); //Calculate base damage from modifier and Actor's skill stat
        
        damage = target.calcDamageReceived(actor, damage, DamageType.CHAOS, StatusEffect.TOXIC); //Calculate damage dealt
        
        //If the damage wasn't completely negated, deal the damage to the target
        if (damage > 0){
            target.dealDamage(damage);
            System.out.println(target.getName() + " took " + damage + " damage from Dragon Skin!");
        }
    }
}