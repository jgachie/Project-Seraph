/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Spells;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.Enemies.Enemy;
import Enums.DamageType;
import Enums.StatusEffect;
import UI.UITextBox;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class MagicBlast extends Spell{
    
    public MagicBlast() {
        super("Magic Blast",
                "Causes a weak explosion with a wide blast radius, inflicting damage on all enemies."
                        + " If the spell fails, damage is inflicted upon the user's party. (Costs 40"
                        + " MP)",
                40,
                50,
                5,
                9);
    }
    
    
    @Override
    public void cast(Actor caster, Actor target) {
        int wisdom = caster.getWisdom(); //The caster's wisdom
        Enemy enemy = (Enemy) target; //Cast the target to an enemy object
        int damageBuffer = 0; //Holds the amount of damage the spell does before factoring in individual resistances
        int[] damages = new int[enemy.getEnemyParty().size()]; //An array holding the amount of damage dealt to each individual enemy after factoring in resistances
        
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
            damageBuffer = (int) (baseDamage * (wisdom / 5.0));
        else if (25 <= wisdom && wisdom < 50)
            damageBuffer = (int) (baseDamage * (wisdom / 10.0) + (baseDamage * 2.4));
        else if (wisdom > 50)
            damageBuffer = (int) (baseDamage * (wisdom / 20.0) + (baseDamage * 4.9));
        
        //Iterate through Enemy party and calculate individual resistances for individual damage values, then deal the damage
        for (int i = 0; i < enemy.getEnemyParty().size(); i++){
            Enemy mob = enemy.getEnemyParty().get(i);
            
            //Only do damage calculations if the Enemy is still alive
            if (mob.isAlive()){
                damages[i] = mob.calcDamageReceived(damageBuffer, DamageType.MAGIC, StatusEffect.NONE); //Calculate damage after factoring in resistances
                
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