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
                8,
                13,
                false,
                false);
    }
    
    @Override
    public void cast(Actor caster, Actor target) {
        int damage;
        
        UITextBox.resetBAOS();
        System.out.println(caster.getName() + " prepares to cast the spell...\n");
        Combat.delay();
        
        caster.useMana(manaReq); //Subtract the spell's required mana from the caster's current mana
        
        //If the spell misses, output a failure message and return
        if (!spellHit(caster)){
            System.out.println("...The spell failed!");
            return;
        }
        
        System.out.println("...The spell succeeded!");
        
        //Calculate the damage dealt
        damage = calcDamage(caster);
        
        //Factor in enemy resistances
        damage = target.calcDamageReceived(damage, DamageType.MAGIC, StatusEffect.NONE);
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0){
            System.out.println(target.getName() + " took " + damage + " damage");
            target.dealDamage(damage);
        }
        else
            System.out.println(target.getName() + " blocked the spell!");
    }
}