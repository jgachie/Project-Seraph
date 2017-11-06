/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Specials.Skills.Basic;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Skills.Skill;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Combat.Combat;
import Enums.DamageType;
import Enums.StatusEffect;

/**
 *
 * @author Soup
 */
public class TaintedEdge extends Skill{
    
    public TaintedEdge(){
        super("Tainted Edge",
                "Sariel imbues his weapon with chaos energy and attacks the enemy. Deals a normal amount"
                        + "of damage, but has a chance of poisoning the target. However, if the attack"
                        + " misses, there is a slight chance that Sariel will wind up poisoning himself."
                        + " Both chances scale with Chaos stat. (Cost: 15 CP)",
                15,
                30,
                6,
                0,
                0,
                false,
                false,
                DEFAULT_SPECIAL_WIDTH,
                DEFAULT_SPECIAL_HEIGHT);
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
    public void use(PlayableActor user, Actor target, Handler handler) {
        super.use(user, target, handler);
        
        int damage;
        
        UITextBox.resetBAOS();
        System.out.println(user.getName() + " prepares to use the skill...\n");
        
        if (!skillHit(user)){
            System.out.println(user.getName() + " done goofed!");
            
        }
        
        user.setAttacking(true);
        handler.getCombat().animationDelay();
        user.setAttacking(false);
        user.resetAnimations();
        
        System.out.println("...The skill succeeded!");
        
        //Calculate the damage dealt
        damage = calcDamage(user);
        
        //Factor in enemy resistances
        damage = target.calcDamageReceived(damage, user.getWeapon().getType(), StatusEffect.POISON);
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0){
            System.out.println(target.getName() + " took " + damage + " damage");
            target.dealDamage(damage);
        }
        else
            System.out.println(target.getName() + " blocked the attack!");
    }
}
