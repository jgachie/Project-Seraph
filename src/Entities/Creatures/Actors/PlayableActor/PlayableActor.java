/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities.Creatures.Actors.PlayableActor;

import Entities.Creatures.Actors.Actor;
import Items.Equipment.Weapon;
import Items.Grimoires.Grimoire;
import Items.Tomes.Tome;
import Main.Handler;
import Enums.Stance;
import Enums.Characters;

/**
 * PlayableActors are Actors that the player actually controls (i.e. Zanna; Rynn; the player character himself)
 * @author Soup
 */
public abstract class PlayableActor extends Actor{
    protected int skillpoints; //How many skillpoints the Actor has left
    protected int maxSP; //Actor's max SP
    protected int exp = 0; //How much experience the Actor currently has
    protected int expCap = 100; //The total amount of experience needed for the Actor to level up
    protected int skill; //Actor's skill stat; varies depending on the Actor
    protected Stance stance; //Actor's stance; determines stat bonuses and penalties, as well as the availability of some attacks
    protected Grimoire grimoire; //Actor's currently equipped grimoire; determines which spells Actor can use in battle
    protected Tome tome; //Actor's currently equipped tome; determines which skills Actor can use in battle
    protected final Characters CHARACTER; //The Actor's character value (Sariel, Zanna, Rynn, etc.); determines which equipment the Actor can use, among other things
    
    protected PlayableActor(Handler handler, float x, float y, int width, int height, String name, Characters character,
            Weapon weapon, int level, int hitpoints, int mana, int skillpoints, int strength, int dexterity,
            int wisdom, int intelligence, int luck, int defense, int agility, int skill) {
        super(handler, x, y, width, height, name, weapon, level, hitpoints, mana, strength, dexterity,
                wisdom, intelligence, luck, defense, agility);
        this.CHARACTER = character;
        this.skillpoints = skillpoints;
        this.maxSP = skillpoints;
        this.skill = skill;
        
        //Standard initializations
        stance = Stance.NEUTRAL;
        exp = 0;
        expCap = level * 100;
    }
    
    /**
     * Spellcasting method; determines type of equipped grimoire and calls its castSpell method
     * @param target The Actor being targeted
     * @param spellNum The ordinal number of the spell being cast (should be between 1 and 5)
     */
    public void castSpell(Actor target, int spellNum){
        //Determine the type of the equipped grimoire and cast "grimoire" to that type before calling castSpell method
        switch (grimoire.getType()){
            case BASIC:
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
    }
    
    /**
     * Returns the Actor's stats in an array of integers
     * @return An array containing the Actor's stats
     */
    public int[] getStats(){
        return new int[]{strength, dexterity, wisdom, intelligence, luck, defense, agility, skill};
    }
    
    //GETTERS/SETTERS
    
    public int getExp() {
        return exp;
    }
    
    public void setExp(int exp) {
        this.exp = exp;
    }
    
    public int getExpCap() {
        return expCap;
    }
    
    public int getSkill() {
        return skill;
    }
    
    public void setSkill(int skill) {
        this.skill = skill;
    }
    
    public void setExpCap(int expCap) {
        this.expCap = expCap;
    }

    public int getSkillpoints() {
        return skillpoints;
    }

    public void setSkillpoints(int skillpoints) {
        this.skillpoints = skillpoints;
    }

    public int getMaxSP() {
        return maxSP;
    }

    public void setMaxSP(int maxSP) {
        this.maxSP = maxSP;
    }
    
    public Stance getStance() {
        return stance;
    }
    
    public void setStance(Stance stance) {
        this.stance = stance;
    }
    
    public Grimoire getGrimoire(){
        return grimoire;
    }
    
    public void setGrimoire(Grimoire grimoire){
        this.grimoire = grimoire;
    }
    
    public Tome getTome(){
        return tome;
    }
    
    public void setTome(Tome tome){
        this.tome = tome;
    }
    
    public Characters getCharacter(){
        return CHARACTER;
    }
}