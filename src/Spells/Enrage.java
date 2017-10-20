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
public class Enrage extends Spell{

    public Enrage() {
        super("Enrage",
                "A spell that affects a single target in the user's party. Sends the target into a battle"
                        + " frenzy, increasing their  physical damage output but decreasing their overall"
                        + " accuracy. (Costs 15 MP)",
                15,
                50,
                true,
                false);
    }

    @Override
    public void cast(Actor caster, Actor target) {
        PlayableActor ally = (PlayableActor) target;
        
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
        
        System.out.println(ally.getName() + "'s strength increased by 2, and their dexterity and intelligence"
                + "fell by 2!");
        
        ally.modifyStat("Strength", 2); //Increase strength by 2
        ally.modifyStat("Dexterity", -2); //Decrease dexterity by 2
        ally.modifyStat("Intelligence", -2); //Decrease intelligence by 2
    }
}