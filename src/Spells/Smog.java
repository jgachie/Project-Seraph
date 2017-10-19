/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Spells;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
import UI.UITextBox;

/**
 *
 * @author Soup
 */
public class Smog extends Spell{
    
    public Smog() {
        super("Smog",
                "A spell that generates a thick smokescreen, obscuring the enemies' field of vision "
                        + "and causing their accuracy to decrease. Also has a chance to poison enemy."
                        + " If the spell fails, the party of the user's accuracy decreases instead. "
                        + "(Costs 20 MP)",
                20,
                40,
                0,
                0);
    }
    
    @Override
    public void cast(Actor caster, Actor target) {
        Enemy enemy = (Enemy) target;
        
        UITextBox.resetBAOS();
        System.out.println(caster.getName() + " prepares to cast the spell...\n");
        Combat.delay();
        
        //If the spell misses, output a failure message and return
        if (!spellHit(caster, baseChance)){
            System.out.println("...The spell failed!");
            return;
        }
        
        System.out.println("...The spell succeeded!");
        
        System.out.println("The enemy party's dexterity fell by 2!");
        
        //Decrease the dexterity of each of the enemy's party members by 2
        for (Enemy member : enemy.getEnemyParty())
            member.modifyStat("Dexterity", -2);
    }
}