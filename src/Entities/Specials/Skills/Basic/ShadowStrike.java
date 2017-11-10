/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials.Skills.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
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
public class ShadowStrike extends Skill{
    
    public ShadowStrike(){
        super("Shadow Strike",
                "Sariel enshrouds the battlefield in darkness and launches a sneak attack under the "
                        + "cover of the shadows on a random enemy. If the skill succeeds, the attack "
                        + "will be a guaranteed critical hit. However, due to his decreased visibility, "
                        + "there is also a chance Sariel will accidentally attack himself or one of "
                        + "his party members. This chance decreases as the Chaos stat increases. (Cost: "
                        + "40 CP)",
                40,
                25,
                100,
                0,
                0,
                false,
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
        
        int damage;
        
        UITextBox.resetBAOS();
        System.out.println(user.getName() + " prepares to use the skill...\n");
        
        user.useSkillpoints(pointReq);
        
        Combat.delay();
        
        if (!skillHit(user)){
            System.out.println(user.getName() + " done goofed!");
            return;
        }
        
        user.setAttacking(true);
        handler.getCombat().animationDelay();
        user.setAttacking(false);
        user.resetAnimations();
        
        //Calculate the damage dealt
        damage = calcDamage(user);
        
        /*
        If the skill hits, the user still has a 50/50 chance of hitting either an enemy or an ally;
        this chance scales negatively with the chaos stat
        */
        
        int skill = user.getSkill(); //The user's skill stat
        int accuracy = dieRoll.nextInt(100) + 1; //Roll for accuracy
        boolean success = false; //Whether the attack hit an enemy or an ally; true if it did, false if it didn't
        
        if (skill < 25){
            if (accuracy <= 55 - skill)
                success = false;
            else
                success = true;
        }
        else if (25 <= skill && skill < 50){
            if (accuracy <= 43 - (skill / 2))
                success = false;
            else
                success = true;
        }
        else if (skill >= 50){
            if (accuracy <= 19)
                success = false;
            else
                success = true;
        }
        
        Enemy enemy = (Enemy) target; //Cast the target to an Enemy object so we can retrieve its party
        
        //If the skill succeeded, randomly choose a target from the enemy's party. If it failed, choose from the Player's party
        if (success){
            System.out.println(user.getName() + " hit an enemy!");
            //If the chosen enemy isn't alive, continue to roll the die until a live enemy is chosen
            do{
                int index = dieRoll.nextInt(enemy.getParty().size()); //Roll a die to see which enemy gets attacked
                target = enemy.getParty().get(index); //Set the chosen enemy as the target
            } while (!target.isAlive());
        }
        else{
            System.out.println(user.getName() + " accidentally hit a party member!");
            //If the chosen ally isn't alive, continue to roll the die until a live ally is chosen
            do{
                int index = dieRoll.nextInt(user.getParty().size()); //Roll a die to see which ally gets attacked
                target = user.getParty().get(index); //Set the chosen ally as the target
            } while (!target.isAlive());
        }
        
        //Factor in enemy resistances
        damage = target.calcDamageReceived(user, damage, user.getWeapon().getType(), StatusEffect.NONE);
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0){
            System.out.println(target.getName() + " took " + damage + " damage");
            target.dealDamage(damage);
        }
        else
            System.out.println(target.getName() + " blocked the attack!");
    }
}