/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Tomes;

import Entities.Creatures.Actors.PlayableActor.PlayableActor;
import Items.Equipment.Equipment;
import Enums.Characters;
import Enums.TomeType;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An empty abstract class purely for inheritance purposes (may replace with interface later on)
 * @author Soup
 */
public abstract class Tome extends Equipment{
    protected final TomeType TYPE; //The tome's type
    
    public Tome(BufferedImage texture, String name, int ID, int[] statReqs, ArrayList<Characters> users, TomeType type) {
        super(texture, name, ID, statReqs, users);
        this.TYPE = type;
    }
    
    @Override
    public void equip(PlayableActor actor){
        if (checkChar(actor.getCharacter()) && checkStats(actor.getStats()))
            actor.setTome(this);
    }
    
    public TomeType getType(){
        return TYPE;
    }
}
