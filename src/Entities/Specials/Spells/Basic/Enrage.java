/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Specials.Spells.Basic;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Spells.Spell;
import Enums.Stat;
import Main.Handler;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
                0,
                0,
                0,
                true,
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
    public void cast(PlayableActor caster, Actor target, Handler handler) {
        super.cast(caster, target, handler);
        PlayableActor ally = (PlayableActor) target;
        
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
        
        ally.modifyStat(Stat.STRENGTH, 2); //Increase strength by 2
        ally.modifyStat(Stat.DEXTERITY, -2); //Decrease dexterity by 2
        ally.modifyStat(Stat.INTELLIGENCE, -2); //Decrease intelligence by 2
        
        System.out.println(ally.getName() + "'s Strength increased by 2, and their Dexterity and Intelligence"
                + "fell by 2!");
    }
}