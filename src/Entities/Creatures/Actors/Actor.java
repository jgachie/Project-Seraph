/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors;

import Entities.Creatures.Creature;
import Main.Handler;
import java.io.*;

/**
 * Actors are creatures that participate in combat; this includes the player character, party members,
 * and enemies.
 * @author Soup
 */
public abstract class Actor extends Creature implements Serializable{
    //Input streams for saving the actor to a file
    private transient static FileInputStream fileIn;
    private transient static ObjectInputStream objectIn;
    
    //Output streams for loading the actor into the game from a file
    private transient FileOutputStream fileOut;
    private transient ObjectOutputStream objectOut;
    
    protected String name; //Actor's name
    //protected Weapon weapon; //Actor's currently equipped weapon; will be implemented later when Weapon class is created
    protected int level = 1; //Actor's level
    protected int exp = 0; //How much experience the actor currently has
    protected int expCap = 100; //The total amount of experience needed for the actor to level up
    protected int hitpoints; //How many hitpoints the actor has left
    protected int maxHP; //Actor's max HP
    protected int mana; //How much mana the actor has left
    protected int maxMP; //Actor's max MP
    protected int strength; //Actor's strength stat; determines power of physical attacks
    protected int dexterity; //Actor's dexterity stat; determines accuracy of all attacks and physical critical hit rate
    protected int wisdom; //Actor's wisdom stat; determines power of magical attacks and magical critical hit rate
    protected int intelligence; //Actor's intelligence stat; determines efficacy of support magic and magical defense state
    protected int luck; //Actor's luck stat; determines item/gold drops and all critical hit rates
    protected int defense; //Actor's defense stat; determines physical defense stats
    protected int evasion; //Actor's evasion stat; determines evasion/flee rate
    protected int skill; //Actor's skill stat; varies depending on the actor
    protected int slashDef; //Actor's slash defense stat; determines damage absorbed from slash attacks
    protected int stabDef; //Actor's stab defense stat; determines damage absorbed from stab attacks
    protected int crushDef; //Actor's crush defense stat; determines damage absorbed from crush attacks
    protected int pierceDef; //Actor's pierce defense stat; determines damage absorbed from pierce attacks (mostly ranged attacks)
    protected int magicDef; //Actor's magic defense stat; determines damage absorbed from non-elemental magic attacks
    protected int fireDef; //Actor's fire defense stat; determines damage absorbed from fire attacks
    protected int waterDef; //Actor's water defense stat; determines damage absorbed from water attacks
    protected int earthDef; //Actor's earth defense stat; determines damage absorbed from earth attacks
    protected int lightningDef; //Actor's lightning defense stat; determines damage absorbed from lightning attacks
    protected boolean alive = true; //Whether or not the actor is alive
    protected boolean party; //Whether or not the actor is currently in the player's party (for the player this is always set to "true")
    
    //Constructor to create a new actor for the first time
    public Actor(Handler handler, float x, float y, int width, int height, String name, /*Weapon weapon,*/
            int level, int hitpoints, int mana, int strength, int dexterity, int wisdom, int intelligence,
            int luck, int defense, int evasion, int skill, boolean party){
        super(handler, x, y, width, height);
        this.name = name;
        //this.weapon = weapon;
        this.level = level;
        exp = 0;
        expCap = level * 100;
        this.hitpoints = hitpoints;
        this.maxHP = hitpoints;
        this.mana = mana;
        this.maxMP = mana;
        this.strength = strength;
        this.dexterity = dexterity;
        this.wisdom = wisdom;
        this.intelligence = intelligence;
        this.luck = luck;
        this.defense = defense;
        this.evasion = evasion;
        this.skill = skill;
        this.party = party;
    }
    
    //Constructor to reload an actor from saved state
    public Actor(Handler handler, float x, float y, int width, int height){
        super(handler, x, y, width, height);
    }
    
    /**
     * Saves this version of an actor to an object file
     * @param actor The actor to be saved
     */
    protected void save(Actor actor, String name){
        try{
            fileOut = new FileOutputStream("ActorSaves/" + name);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(actor);
            objectOut.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Loads a previous version of an actor from a saved object file
     * @param name The name of the actor, used to select the corresponding object file
     * @return The loaded actor object
     */
    protected static Actor load(String name){
        try{
            fileIn = new FileInputStream("ActorSaves/" + name);
            objectIn = new ObjectInputStream(fileIn);
            Actor actor = (Actor) objectIn.readObject();
            objectIn.close();
            return actor;
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    public abstract void save();
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getExp() {
        return exp;
    }
    
    public void setExp(int exp) {
        this.exp = exp;
    }
    
    public int getExpCap() {
        return expCap;
    }
    
    public void setExpCap(int expCap) {
        this.expCap = expCap;
    }
    
    public int getHitpoints() {
        return hitpoints;
    }
    
    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }
    
    public int getMaxHP() {
        return maxHP;
    }
    
    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
    
    public int getMana() {
        return mana;
    }
    
    public void setMana(int mana) {
        this.mana = mana;
    }
    
    public int getMaxMP() {
        return maxMP;
    }
    
    public void setMaxMP(int maxMP) {
        this.maxMP = maxMP;
    }
    
    public int getStrength() {
        return strength;
    }
    
    public void setStrength(int strength) {
        this.strength = strength;
    }
    
    public int getDexterity() {
        return dexterity;
    }
    
    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
    
    public int getWisdom() {
        return wisdom;
    }
    
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }
    
    public int getIntelligence() {
        return intelligence;
    }
    
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }
    
    public int getLuck() {
        return luck;
    }
    
    public void setLuck(int luck) {
        this.luck = luck;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public int getEvasion() {
        return evasion;
    }
    
    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }
    
    public int getSkill() {
        return skill;
    }
    
    public void setSkill(int skill) {
        this.skill = skill;
    }
    
    public int getSlashDef() {
        return slashDef;
    }
    
    public void setSlashDef(int slashDef) {
        this.slashDef = slashDef;
    }
    
    public int getStabDef() {
        return stabDef;
    }
    
    public void setStabDef(int stabDef) {
        this.stabDef = stabDef;
    }
    
    public int getCrushDef() {
        return crushDef;
    }
    
    public void setCrushDef(int crushDef) {
        this.crushDef = crushDef;
    }
    
    public int getPierceDef() {
        return pierceDef;
    }
    
    public void setPierceDef(int pierceDef) {
        this.pierceDef = pierceDef;
    }
    
    public int getMagicDef() {
        return magicDef;
    }
    
    public void setMagicDef(int magicDef) {
        this.magicDef = magicDef;
    }
    
    public int getFireDef() {
        return fireDef;
    }
    
    public void setFireDef(int fireDef) {
        this.fireDef = fireDef;
    }
    
    public int getWaterDef() {
        return waterDef;
    }
    
    public void setWaterDef(int waterDef) {
        this.waterDef = waterDef;
    }
    
    public int getEarthDef() {
        return earthDef;
    }
    
    public void setEarthDef(int earthDef) {
        this.earthDef = earthDef;
    }
    
    public int getLightningDef() {
        return lightningDef;
    }
    
    public void setLightningDef(int lightningDef) {
        this.lightningDef = lightningDef;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public boolean isParty() {
        return party;
    }
    
    public void setParty(boolean party) {
        this.party = party;
    }
}