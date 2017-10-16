/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Equipment;

import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Items.Item;
import Enums.Characters;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public abstract class Equipment extends Item{
    protected final ArrayList<Characters> USERS; //An ArrayList of the characters capable of using this grimoire
    protected final int[] STATREQS; //The stat requirements for equipping this weapon; order of stats is Strength, Dexterity, Wisdom, Intelligence, Luck, Defense, Evasion, Skill
    
    
    public Equipment(BufferedImage texture, String name, int ID, int[] statReqs, ArrayList<Characters> users) {
        super(texture, name, ID);
        this.STATREQS = statReqs;
        this.USERS = users;
    }
    
    /**
     * Equips this piece of equipment to an Actor, provided the Actor is capable of doing so; implemented
     * in each subclass differently, as the set method for each type of equipment is unique
     * @param actor The Actor equipping the piece of equipment
     */
    public abstract void equip(PlayableActor actor);
    
    /**
     * Checks to make sure that a given Actor has the necessary stats to equip this piece of equipment
     * @param stats The Actor's stats
     * @return True if the Actor can equip it; false if otherwise
     */
    protected boolean checkStats(int[] stats){
        //Iterate over both arrays simultaneously to compare Actor's stats to the equipment's stat requirements
        for (int i = 0; i < STATREQS.length; i++){
            //If any one of the Actor's stats is less than the corresponding stat requirement, return false
            if (STATREQS[i] > stats[i])
                return false;
        }
        
        //If all of the Actor's stats meet the equipment's stat requirments, return true
        return true;
    }
    
    /**
     * Checks to make sure that a given character is allowed to equip this piece of equipment
     * @param character The character trying to equip this piece of equipment
     * @return True if the character can equip it; false if otherwise
     */
    protected boolean checkChar(Characters character){
        //Check the given character against the list of valid characters. If the character is valid, return true; otherwise, return false
        return USERS.stream().anyMatch((person) -> (character == person));
    }
}