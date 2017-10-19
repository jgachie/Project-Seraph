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
import Graphics.Assets;
import Spells.*;
import java.util.Arrays;

/**
 * An empty abstract class purely for inheritance purposes (may replace with interface later on)
 * @author Soup
 */
public class Grimoire extends Equipment{
    //Hard-code all different types of weapons here
    public static Grimoire[] grimoires = new Grimoire[256];
    public static Grimoire basicGrimoire = new Grimoire(Assets.stone, "Basic Grimoire", "G001", new int[]{0, 0, 3, 3, 0, 0, 0, 0},
            GrimoireType.BASIC, new ArrayList<Spell>(){{
                add(new MagicBullet()); add(new MagicBlast()); add(new Refresh()); add(new Enrage()); add(new Smog());
            }});
    
    protected final GrimoireType TYPE; //The grimoire's type
    protected final ArrayList<Spell> spells; //The spells the grimoire contains
    
    protected Grimoire(BufferedImage texture, String name, String ID, int[] statReqs, GrimoireType type,
            ArrayList<Spell> spells, Characters... users) {
        super(texture, name, ID, statReqs, new ArrayList<Characters>(Arrays.asList(users)));
        this.TYPE = type;
        this.spells = spells;
    }
    
    
    
    @Override
    public void equip(PlayableActor actor){
        if (checkChar(actor.getCharacter()) && checkStats(actor.getStats()))
            actor.setGrimoire(this);
    }
    
    public GrimoireType getType() {
        return TYPE;
    }
    
    public ArrayList<Spell> getSpells() {
        return spells;
    }
}