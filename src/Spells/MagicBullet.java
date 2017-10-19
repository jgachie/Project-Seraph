/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Spells;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Enums.DamageType;
import Enums.StatusEffect;
import UI.UITextBox;

/**
 *
 * @author Soup
 */
public class MagicBullet extends Spell{
    
    public MagicBullet() {
        super("Magic Bullet",
                "Hurls a small, concentrated mass of magical energy at a single target, inflicting damage."
                        + " (Costs 20 MP)",
                20,
                65,
                10,
                12);
    }
    
    @Override
    public void cast(Actor caster, Actor target) {
        int damage = 0;
        int wisdom = caster.getWisdom();
        
        UITextBox.resetBAOS();
        System.out.println(caster.getName() + " prepares to cast the spell...\n");
        Combat.delay();
        
        //If the spell misses, output a failure message and return
        if (!spellHit(caster, baseChance)){
            System.out.println("...The spell failed!");
            return;
        }
        
        System.out.println("...The spell succeeded!");
        
        //Calculate damage dealt
        if (wisdom < 25)
            damage = (int) (baseDamage * (wisdom / 5.0));
        else if (25 <= wisdom && wisdom < 50)
            damage = (int) (baseDamage * (wisdom / 10.0) + (baseDamage * 2.4));
        else if (wisdom > 50)
            damage = (int) (baseDamage * (wisdom / 20.0) + (baseDamage * 4.9));
        
        damage = target.calcDamageReceived(damage, DamageType.MAGIC, StatusEffect.NONE);
        
        if (damage > 0){
            System.out.println(target.getName() + " took " + damage + " damage");
            target.dealDamage(damage);
        }
        else{
            System.out.println(target.getName() + " blocked the spell!");
        }
    }
}