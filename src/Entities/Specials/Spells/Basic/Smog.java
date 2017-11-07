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
import Entities.Specials.Spells.Spell;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Soup
 */
public class Smog extends Spell{
    
    public Smog() {
        super("Smog",
                "A spell that generates a thick smokescreen, obscuring the enemy party's field of vision"
                        + " and causing their accuracy to decrease. Also has a chance to poison enemy."
                        + " If the spell fails, the party of the user's accuracy decreases instead. "
                        + "(Costs 20 MP)",
                20,
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
    public void cast(PlayableActor caster, Actor target, Handler handler) {
        super.cast(caster, target, handler);
        Enemy enemy = (Enemy) target;
        
        UITextBox.resetBAOS();
        System.out.println(caster.getName() + " prepares to cast the spell...\n");
        Combat.delay();
        
        caster.useMana(pointReq); //Subtract the spell's required mana from the caster's current mana
        
        //If the spell misses, output a failure message and return
        if (!spellHit(caster)){
            System.out.println("...The spell failed!");
            return;
        }
        
        System.out.println("...The spell succeeded!");
        
        System.out.println("The enemy party's dexterity fell by 2!");
        
        //Decrease the dexterity of each of the enemy's party members by 2
        for (Enemy member : enemy.getParty())
            member.modifyStat("Dexterity", -2);
    }
}