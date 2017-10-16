/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Grimoires;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Items.Equipment.Equipment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Enums.Characters;
import Enums.GrimoireType;

/**
 * An empty abstract class purely for inheritance purposes (may replace with interface later on)
 * @author Soup
 */
public abstract class Grimoire extends Equipment{
    protected final GrimoireType TYPE; //The grimoire's type+
    protected String[] spellNames; //The names of the spells
    protected String[] spellDescs; //The spells' descriptions
    
    public Grimoire(BufferedImage texture, String name, int ID, int[] statReqs, ArrayList<Characters> users, GrimoireType type) {
        super(texture, name, ID, statReqs, users);
        this.TYPE = type;
    }
    
    public abstract void castSpell(Actor target, int spellNum);
    
    @Override
    public void equip(PlayableActor actor){
        if (checkChar(actor.getCharacter()) && checkStats(actor.getStats()))
            actor.setGrimoire(this);
    }
    
    public GrimoireType getType() {
        return TYPE;
    }

    public String[] getSpellNames() {
        return spellNames;
    }

    public String[] getSpellDescs() {
        return spellDescs;
    }
}
