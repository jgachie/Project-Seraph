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
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Nightmare extends Skill{
    
    public Nightmare(){
        super("Nightmare",
                "Sariel looks into his enemies' minds and uncovers their deepest fears, and then bombards "
                        + "them with terrifying images. If successful, the enemy party will be intimidated "
                        + "into dropping its guard a bit. However, if the skill fails, their resolve "
                        + "will increase as a result. (Cost: 10 CP)",
                10,
                40,
                0,
                0,
                0,
                false,
                true,
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
        
        Enemy enemy = (Enemy) target; //Cast the target to an Enemy object so we can retrieve its party
        
        UITextBox.resetBAOS();
        System.out.println(user.getName() + " prepares to use the skill...\n");
        
        user.useSkillpoints(pointReq);
        
        Combat.delay();
        
        if (!skillHit(user)){
            System.out.println(user.getName() + " done goofed!");
            
            //Run through the enemy party and, if the enemy is alive, increase their strength by 3
            for (int i = 0; i < enemy.getParty().size(); i++){
                if (enemy.getParty().get(i).isAlive()){
                    enemy.getParty().get(i).modifyStat("Strength", 3);
                    System.out.println(enemy.getParty().get(i).getName() + "'s strength increased by 3!");
                }
            }
            
            return;
        }
        
        System.out.println("...The skill succeeded!");
        
        //Run through the enemy party and, if the enemy is alive, decrease their defense by 2
        for (int i = 0; i < enemy.getParty().size(); i++){
            if (enemy.getParty().get(i).isAlive()){
                enemy.getParty().get(i).modifyStat("Defense", -2);
                System.out.println(enemy.getParty().get(i).getName() + "'s defense decreased by 2!");
            }
        }
    }
}