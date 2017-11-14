/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors.PlayableActors;

import Combat.Combat;
import Entities.Creatures.Actors.Actor;
import Items.Equipment.Weapon;
import Items.Grimoires.Grimoire;
import Items.Tomes.Tome;
import Main.Handler;
import Enums.Stance;
import Enums.Characters;
import Enums.Stat;
import Enums.States;
import States.PauseState;
import UI.UITextBox;
import java.util.ArrayList;

/**
 * PlayableActors are Actors that the player actually controls (i.e. Zanna; Rynn; the player character him/herself)
 * @author Soup
 */
public abstract class PlayableActor extends Actor{
    protected int skillpoints; //How many skillpoints the Actor has left
    protected int maxSP; //Actor's max SP
    protected int expCap = 100; //The total amount of experience needed for the Actor to level up
    protected int skill; //Actor's skill stat; varies depending on the Actor
    protected Stance stance; //Actor's stance; determines stat bonuses and penalties, as well as the availability of some attacks
    protected Grimoire grimoire; //Actor's currently equipped grimoire; determines which spells Actor can use in battle
    protected Tome tome; //Actor's currently equipped tome; determines which skills Actor can use in battle
    protected final Characters CHARACTER; //The Actor's character value (Sariel, Zanna, Rynn, etc.); determines which equipment the Actor can use, among other things
    protected ArrayList<PlayableActor> party; //The Actor's party
    
    //TEMPORARY COMBAT FIELDS
    protected boolean casting = false;
    
    private int[] statSave; //An array holding the Actor's original, unmodified stats before entering combat
    
    protected PlayableActor(Handler handler, float x, float y, int width, int height, String name, Characters character,
            Weapon weapon, int level, int hitpoints, int mana, int skillpoints, int exp, int strength,
            int dexterity, int wisdom, int intelligence, int luck, int defense, int agility, int skill,
            ArrayList<PlayableActor> party) {
        super(handler, x, y, width, height, name, weapon, level, hitpoints, mana, exp, strength, dexterity,
                wisdom, intelligence, luck, defense, agility);
        this.CHARACTER = character;
        this.skillpoints = skillpoints;
        this.maxSP = skillpoints;
        this.skill = skill;
        this.party = party;
        this.statSave = new int[8];
        this.grimoire = Grimoire.basicGrimoire;
        this.tome = Tome.basicTome;
        
        //Standard initializations
        stance = Stance.NEUTRAL;
        exp = 0;
        expCap = level * 100;
    }
    
    /**
     * Accumulates gained experience points into total experience points, and calls levelUp method if
     * the experience cap is reached
     * @param experience The amount of experience points gained
     */
    public void gainExp(int experience){
        exp += experience; //Add the experience points to the total
        
        //If the Actor has gained enough experience points, switch over to the level screen in the Pause state so they can level up
        //This is a while loop to account for the off chance that the Actor gained enough experience to level up more than once
        while (exp >= expCap){
            UITextBox.resetBAOS();
            System.out.println(name + " leveled up!");
            Combat.delay();
            PauseState pauseState = (PauseState) handler.getGame().getState(States.PAUSE); //Get the game's pause state
            pauseState.setSubmenu("Level Up"); //Set the pause state up to load the "level up" menu
            pauseState.setActor(this); //Set the actor being leveled up
            pauseState.setMenuSwitch(true); //Set the pause state's menu switch flag to true
            handler.getGame().setState(States.PAUSE); //Switch over to the pause state
            
            //Make the thread wait until the Actor is done leveling up
            synchronized (this){
                try{
                    this.wait();
                } catch (InterruptedException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Increases the Actor's level, and allows the player to choose a stat to level up
     * @param stat The stat to be leveled up
     */
    public void levelUp(Stat stat){
        level++; //Increase the Actor's level by one
        exp -= expCap; //Subtract the cap from the Actor's experience points and set "exp" to whatever remains
        expCap *= (1.5 + ((level - 1) * .1)); //The cap increases with each level by a factor of 1.5 + .1 * whatever level the character is now
        
        //Level up the chosen stat
        switch(stat){
            case STRENGTH:
                strength++;
                break;
            case DEXTERITY:
                dexterity++;
                break;
            case WISDOM:
                wisdom++;
                maxMP += 10; //Increasing Wisdom also increases max MP
                break;
            case INTELLIGENCE:
                intelligence++;
                
                //Increasing Intelligence also increases magic defenses
                magicDef += 10;
                fireDef += 10;
                iceDef += 10;
                earthDef += 10;
                lightningDef += 10;
                break;
            case LUCK:
                luck++;
                break;
            case DEFENSE:
                defense++;
                
                //Increasing Defense also increases max HP and physical defenses
                maxHP += 10;
                slashDef += 10;
                stabDef += 10;
                crushDef += 10;
                pierceDef += 10;
                break;
            case AGILITY:
                agility++;
                
                //Increasing Agility also increases stats resistances
                poisonRes += 10;
                stunRes += 10;
                freezeRes += 10;
                break;
            case SKILL:
                skill++;
                
                //Increasing Skill also increases max SP and chaos defenses
                maxSP += 10;
                chaosDef += 10;
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
        
        //Restore hitpoints, mana, and skillpoints to new maximums
        hitpoints = maxHP;
        mana = maxMP;
        skillpoints = maxSP;
        
        //Give the original thread the go-ahead to continue
        synchronized (this){
            notifyAll();
        }
    }
    
    //COMBAT METHODS
    
    /**
     * Spellcasting method; determines the spell to be cast from the currently equipped grimoire's list
     * of spells and calls its cast method
     * @param target The Actor being targeted
     * @param spellNum The ordinal number of the spell being cast (should be between 1 and 5)
     * @param handler The handler
     */
    public void castSpell(Actor target, int spellNum, Handler handler){
        grimoire.getSpells().get(spellNum).cast(this, target, handler);
    }
    
    /**
     * Skill using method; determines the skill to be used from the currently equipped tome's list of
     * skills and calls it use method
     * @param target The Actor being targeted
     * @param skillNum The ordinal number of the skill being used (should be between 1 and 5)
     * @param handler The handler
     */
    public void useSkill(Actor target, int skillNum, Handler handler){
        tome.getSkills().get(skillNum).use(this, target, handler);
    }
    
    public void useItem(Actor target, int itemID){
        //Will implement later
    }
    
    /**
     * Subtracts from the Actor's mana as a result of using a spell
     * @param SP The amount of skillpoints the used skill takes
     */
    public void useSkillpoints(int SP){
        skillpoints -= SP; //Subtract the amount of skillpoints the skill uses from the Actor's current skillpoints
        
        //If the Actor's skillpoints falls to or below 0 (which should never happen), reset skillpoints to 0
        if (skillpoints <= 0)
            skillpoints = 0;
    }
    
    @Override
    public void modifyStat(Stat stat, int modify){
        switch (stat){
            case STRENGTH:
                strength += modify;
                break;
            case DEXTERITY:
                dexterity += modify;
                break;
            case WISDOM:
                wisdom += modify;
                break;
            case INTELLIGENCE:
                intelligence += modify;
                break;
            case LUCK:
                luck += modify;
                break;
            case DEFENSE:
                defense += modify;
                break;
            case AGILITY:
                agility += modify;
                break;
            case SKILL:
                skill += modify;
                break;
            default:
                //Should never get here; if you do, FUCKING PANIC
                break;
        }
    }
    
    /**
     * Saves the Actor's unmodified stats into an array before starting combat
     */
    public void saveStats(){
        statSave[0] = strength;
        statSave[1] = dexterity;
        statSave[2] = wisdom;
        statSave[3] = intelligence;
        statSave[4] = luck;
        statSave[5] = defense;
        statSave[6] = agility;
        statSave[7] = skill;
    }
    
    /**
     * Loads the Actor's unmodified stats from the saved array after ending combat
     */
    public void loadStats(){
        strength = statSave[0];
        dexterity = statSave[1];
        wisdom = statSave[2];
        intelligence = statSave[3];
        luck = statSave[4];
        defense = statSave[5];
        agility = statSave[6];
        skill = statSave[7];
    }
    
    /**
     * Returns the Actor's stats in an array of integers
     * @return An array containing the Actor's stats
     */
    public int[] getStats(){
        return new int[]{strength, dexterity, wisdom, intelligence, luck, defense, agility, skill};
    }
    
    //MISC METHODS
    
    /**
     * PlayableActor save method; calls the save supermethod with a specific filename, provided by value
     * of PlayableActor's character
     */
    public void save(){
        save(this, CHARACTER);
    }
    
    /**
     * Resets all single-run animations used during combat
     */
    public abstract void resetAnimations();
    
    //GETTERS/SETTERS
    
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
    
    public Grimoire getGrimoire() {
        return grimoire;
    }
    
    public void setGrimoire(Grimoire grimoire) {
        this.grimoire = grimoire;
    }
    
    public Tome getTome() {
        return tome;
    }
    
    public void setTome(Tome tome) {
        this.tome = tome;
    }
    
    public ArrayList<PlayableActor> getParty() {
        return party;
    }
    
    public void setParty(ArrayList<PlayableActor> party) {
        this.party = party;
    }
    
    public Characters getCharacter() {
        return CHARACTER;
    }
    
    public boolean isCasting() {
        return casting;
    }
    
    public void setCasting(boolean casting) {
        this.casting = casting;
    }
}