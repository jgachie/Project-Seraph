/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Spells;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import UI.UITextBox;

/**
 *
 * @author Soup
 */
public class Refresh extends Spell{

    public Refresh() {
        super("Refresh",
                "A low-level healing spell that leaves a single target feeling rested and ready for "
                        + "battle once more. (Costs 35 MP)",
                35,
                60,
                10,
                5,
                10,
                true,
                false);
    }

    @Override
    public void cast(Actor caster, Actor target) {
        int intelligence = caster.getIntelligence(); //The caster's intelligence
        int restore = 0; //The amount of hitpoints restored by the spell
        int baseRestore = dieRoll.nextInt(maxDamage + 1) + minDamage;
        
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
        
        //Calculate number of hitpoints to be restored
        if (intelligence < 25)
            restore = (int) (baseRestore * (intelligence / 5.0));
        else if (25 <= intelligence && intelligence < 50)
            restore = (int) (baseRestore * (intelligence / 10.0) + (baseRestore * 2.4));
        else if (intelligence > 50)
            restore = (int) (baseRestore * (intelligence / 20.0) + (baseRestore * 4.9));
        
        //If the spell was critical, multiply the number of hitpoints restored by 2
        if (spellCrit(caster)){
            System.out.println("It was a critical hit!");
            restore *= 2;
        }
        
        System.out.println(target.getName() + " recovered " + restore + " hitpoints!");
        target.heal(restore); //Heal the target
    }
}