/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors;

import Enums.StatusEffect;
import Enums.DamageType;
import Entities.Creatures.Creature;
import Items.Equipment.Weapon;
import Main.Handler;
import java.io.*;
import java.util.Random;

/**
 * Actors are creatures that participate in combat; this includes the player character, party members,
 * and enemies.
 * @author Soup
 */
public abstract class Actor extends Creature{
    //Input streams for loading the Actor object during deserialization
    private transient static FileInputStream fileIn;
    private transient static ObjectInputStream objectIn;
    
    //Output streams for saving the Actor object during serialization
    private transient FileOutputStream fileOut;
    private transient ObjectOutputStream objectOut;
    
    private static Random dieRoll = new Random(); //A Random object for determining various outcomes
    
    protected String name; //Actor's name
    protected Weapon weapon; //Actor's currently equipped weapon
    protected int level; //Actor's level
    protected int hitpoints; //How many hitpoints the Actor has left
    protected int maxHP; //Actor's max HP
    protected int mana; //How much mana the Actor has left
    protected int maxMP; //Actor's max MP
    protected int strength; //Actor's strength stat; determines power of physical attacks
    protected int dexterity; //Actor's dexterity stat; determines accuracy of physical attacks and resistance stats
    protected int wisdom; //Actor's wisdom stat; determines power of magical attacks and max MP
    protected int intelligence; //Actor's intelligence stat; determines efficacy of support magic and magical defense stats
    protected int luck; //Actor's luck stat; determines item/gold drops and all critical hit rates
    protected int defense; //Actor's defense stat; determines physical defense stats and max HP
    protected int evasion; //Actor's evasion stat; determines evasion/flee success rate
    protected int slashDef; //Actor's slash defense stat; determines damage absorbed from slash attacks
    protected int stabDef; //Actor's stab defense stat; determines damage absorbed from stab attacks
    protected int crushDef; //Actor's crush defense stat; determines damage absorbed from crush attacks
    protected int pierceDef; //Actor's pierce defense stat; determines damage absorbed from pierce attacks (mostly ranged attacks)
    protected int magicDef; //Actor's magic defense stat; determines damage absorbed from non-elemental magic attacks
    protected int fireDef; //Actor's fire defense stat; determines damage absorbed from fire attacks
    protected int iceDef; //Actor's water defense stat; determines damage absorbed from water attacks
    protected int earthDef; //Actor's earth defense stat; determines damage absorbed from earth attacks
    protected int lightningDef; //Actor's lightning defense stat; determines damage absorbed from lightning attacks
    protected int poisonRes; //Actor's resistance to poison/toxic; determines chance of being poisoned/toxined (max of 500)
    protected int stunRes; //Actor's resistance to stun; determines chance of being stunned (max of 500)
    protected int freezeRes; //Actor's resistance to freeze; determines chance of being frozen (max of 500)
    protected StatusEffect status; //The status effect Actor is currently afflicted by
    //Replace "status" with ArrayList of StatusEffects so an Actor can be afflicted by more than one status effect at a time
    protected boolean alive = true; //Whether or not the Actor is alive
    
    protected Actor(Handler handler, float x, float y, int width, int height, String name, Weapon weapon,
            int level, int hitpoints, int mana, int strength, int dexterity, int wisdom, int intelligence,
            int luck, int defense, int evasion){
        super(handler, x, y, width, height);
        this.name = name;
        this.weapon = weapon;
        this.level = level;
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
        
        //Standard initializations
        poisonRes = 50;
        stunRes = 50;
        freezeRes = 50;
        status = StatusEffect.NONE;
    }
    
    //ATTACK METHODS
    
    /**
     * Actor attack method; calls methods for calculating damage dealt and received, as well as the
     * dealDamage method itself
     * @param target The Actor being targeted
     */
    public void attack(Actor target){
        int damage;
        
        //If the attack misses, return; otherwise, calculate initial damage dealt and then apply evasion and defense modifiers
        if (!attackHit())
            return;
        else{
            damage = calcAttackDamage();
            damage = target.calcDamageReceived(damage, weapon.getType(), weapon.getEffect());
        }
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0)
            target.dealDamage(damage);
    }
    
    /**
     * Calculates physical attack damage based weapon base damage and Actor's stats
     *
     * Damage is calculated as follows: The base damage is obtained from a die roll, the range of which
     * is set by the equipped weapon's minimum and maximum damage values. Attack multiplier is then
     * applied based on strength level (implemented in tiers). For second and third tiers, additional
     * damage is added to the initial calculation to make up for loss when attack multiplier decreases.
     * After initial damage calculation is done, a critical multiplier is applied if the attack was
     * critical.
     * @return The amount of damage dealt
     */
    private int calcAttackDamage(){
        int baseDamage; //The base damage of the weapon
        int damage = 0; //The amount of damage dealt
        
        baseDamage = dieRoll.nextInt(weapon.getMaxDamage() + 1) + weapon.getMinDamage(); //Roll for base damage using weapon min and max damages as range
        
        /*
        Determine damage dealt by a combination of strength and base damage based on die roll. With
        base strength level of 5, attack muliplier is effectively 1, but increases by a factor of .2
        with each level until level 25. At 25, the growth of the multiplier slows to .1, and at level
        50, it falls to .05.
        */
        
        if (strength < 25)
            damage = (int) (baseDamage * (strength / 5.0));
        else if (25 <= strength && strength < 50){
            damage = (int) (baseDamage * (strength / 10.0) + (baseDamage * 2.4));
        }
        else if (strength > 50){
            damage = (int) (baseDamage * (strength / 20.0) + (baseDamage * 4.9));
        }
        
        //If the attack was critical, multiply the damage by 1.5
        if (attackCrit())
            damage *= 1.5;
        
        return damage;
    }
    
    /**
     * Calculates total damage received by Actor by applying defense modifiers to damage dealt. Also
     * applies any status effects accompanied by attack.
     * @param dmg The amount of damage dealt, before applying defense modifiers
     * @param type The type of damage dealt
     * @param effect The status effect to be inflicted
     * @return The total damage received by the Actor
     */
    private int calcDamageReceived(int damage, DamageType type, StatusEffect effect){
        /*
        Defense doesn't affect damage reduction directly; increasing defense increases damage type defenses
        (in addition to max hp), which in turn are used in damage reduction calculations
        */
        
        switch (type){
            case SLASH:
                //I'll take care of this shit later
                break;
            case STAB:
                break;
            case CRUSH:
                break;
            case PIERCE:
                break;
            case MAGIC:
                break;
            case FIRE:
                break;
            case ICE:
                break;
            case EARTH:
                break;
            case LIGHTNING:
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
        }
        
        //If there is an applicable status effect, run it against Actor's resistances and apply it
        if (effect != StatusEffect.NONE)
            statusHit(effect);
        
        return damage;
    }
    
    /**
     * Takes calculated damage and subtracts it from Actor's hitpoints
     * @param damage The amount of damage dealt
     */
    private void dealDamage(int damage){
        hitpoints -= damage; //Subract damage from Actor's hitpoints
        
        //If the Actor's hitpoints fall to or below 0, the Actor is dead
        if (hitpoints <= 0){
            hitpoints = 0;
            alive = false;
        }
    }
    
    /**
     * Uses dexterity to determine whether attack hit or not
     * @return True if the attack hit; false if it missed
     */
    private boolean attackHit(){
        boolean hit = false; //Whether or not the attack hit; initialized to false
        int accuracy = dieRoll.nextInt(100); //Roll for accuracy
        
        /*
        Determines hit success by a combination of die roll and dexterity. With base dexterity level
        of 5, base chance to hit will be 55%. Dexterity efficacy gradually diminishes as stat level
        increases; by level 25, it's half as effective, and by level 50, it's no longer a factor at
        all, and chances of missing remain at a constant 14%.
        */
        if (dexterity < 25){
            if (accuracy <= 50 - dexterity)
                hit = false;
            else
                hit = true;
        }
        else if (25 <= dexterity && dexterity < 50){
            if (accuracy <= 38 - (dexterity / 2))
                hit = false;
            else
                hit = true;
        }
        else if (dexterity >= 50){
            if (accuracy <= 14)
                hit = false;
            else
                hit = true;
        }
        
        return hit;
    }
    
    /**
     * Uses luck to determine whether attack was critical or not
     * @return True if the attack is critical; false if otherwise
     */
    public boolean attackCrit(){
        boolean crit = false; //Whether or not the attack will be critical; initialized to false
        int chance = dieRoll.nextInt(100); //Roll for crit
        
        /*
        Determines crit success by a combination of die roll and luck. With base luck level 5, base
        chance of success is 6%. Luck efficacy gradually diminishes as stat level increases; by level
        25, it's half as effective, and by level 50, it's no longer a factor at all, and chances of
        success remain at a constant 21% (~1/5).
        */
        if (luck < 25){
            if (chance <= 95 - (luck / 3))
                crit = false;
            else
                crit = true;
        }
        else if (25 <= luck && luck < 50){
            if (chance <= 87 - (luck / 6))
                crit = false;
            else
                crit = true;
        }
        else if (luck >= 50){
            if (chance <= 79)
                crit = false;
            else
                crit = true;
        }
        
        return crit;
    }
    
    /**
     * Uses resistances to determine whether status effect takes effect or not
     * @param effect The status effect
     */
    private void statusHit(StatusEffect effect){
        //If the Actor is already afflicted by a status effect, return
        if (status != StatusEffect.NONE)
            return;
        
        int chance = dieRoll.nextInt(500); //Roll for effect chance
        
        switch(effect){
            case POISON:
                if (chance <= 500 - poisonRes)
                    status = effect;
                break;
            case TOXIC:
                if (chance <= 500 - poisonRes)
                    status = effect;
                break;
            case STUN:
                if (chance <= 500 - stunRes)
                    status = effect;
                break;
            case FREEZE:
                if (chance <= 500 - freezeRes)
                    status = effect;
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
    }
    
    //SERIALIZATION METHODS
    
    /**
     * Saves this version of an Actor to an object file
     * @param actor The Actor to be saved
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
     * Loads a previous version of an Actor from a saved object file
     * @param name The name of the Actor, used to select the corresponding object file
     * @return The loaded Actor object
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
    
    //GETTERS/SETTERS
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Weapon getWeapon() {
        return weapon;
    }
    
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
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
    
    public int getIceDef() {
        return iceDef;
    }
    
    public void setIceDef(int iceDef) {
        this.iceDef = iceDef;
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

    public int getPoisonRes() {
        return poisonRes;
    }

    public void setPoisonRes(int poisonRes) {
        this.poisonRes = poisonRes;
    }

    public int getStunRes() {
        return stunRes;
    }

    public void setStunRes(int stunRes) {
        this.stunRes = stunRes;
    }

    public int getFreezeRes() {
        return freezeRes;
    }

    public void setFreezeRes(int freezeRes) {
        this.freezeRes = freezeRes;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public StatusEffect getStatus() {
        return status;
    }

    public void setStatus(StatusEffect status) {
        this.status = status;
    }
}