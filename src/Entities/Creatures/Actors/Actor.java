/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors;

import Combat.Combat;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.StatusEffect;
import Enums.DamageType;
import Entities.Creatures.Creature;
import Entities.Specials.Skills.Basic.DragonSkin;
import Enums.Characters;
import Enums.Stat;
import Items.Equipment.Weapon;
import Main.Handler;
import UI.UITextBox;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
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
    
    //Output streams for saving the Actor object during serialization (could be static, will check later)
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
    protected int exp; //How much experience the Actor currently has
    protected int strength; //Actor's strength stat; determines power of physical attacks
    protected int dexterity; //Actor's dexterity stat; determines accuracy of physical attacks and resistance stats
    protected int wisdom; //Actor's wisdom stat; determines power of magical attacks and max MP
    protected int intelligence; //Actor's intelligence stat; determines efficacy of support magic and magical defense stats
    protected int luck; //Actor's luck stat; determines item/gold drops and all critical hit rates
    protected int defense; //Actor's defense stat; determines physical defense stats and max HP
    protected int agility; //Actor's agility stat; determines flee success rate, attack order, and status resistances
    protected int slashDef = 50; //Actor's slash defense stat; determines damage absorbed from slash attacks
    protected int stabDef = 50; //Actor's stab defense stat; determines damage absorbed from stab attacks
    protected int crushDef = 50; //Actor's crush defense stat; determines damage absorbed from crush attacks
    protected int pierceDef = 50; //Actor's pierce defense stat; determines damage absorbed from pierce attacks (mostly ranged attacks)
    protected int magicDef = 50; //Actor's magic defense stat; determines damage absorbed from non-elemental magic attacks
    protected int chaosDef = 50; //Actor's chaos defense stat; determines damage absorbed from chaos attacks
    protected int fireDef = 50; //Actor's fire defense stat; determines damage absorbed from fire attacks
    protected int iceDef = 50; //Actor's water defense stat; determines damage absorbed from water attacks
    protected int earthDef = 50; //Actor's earth defense stat; determines damage absorbed from earth attacks
    protected int lightningDef = 50; //Actor's lightning defense stat; determines damage absorbed from lightning attacks
    protected int poisonRes = 50; //Actor's resistance to poison/toxic; determines chance of being poisoned/toxined (max of 500)
    protected int stunRes = 50; //Actor's resistance to stun; determines chance of being stunned (max of 500)
    protected int freezeRes = 50; //Actor's resistance to freeze; determines chance of being frozen (max of 500)
    protected transient Runnable action; //A set of combat methods set during the takeTurn phase of combat and run during the action phase
    protected HashMap<StatusEffect, Integer> effects; //A hashmap to hold the Actor's inflicted status effects the duration for which they last in terms of turns
    protected boolean alive = true; //Whether or not the Actor is alive
    
    //TEMPORARY COMBAT FIELDS
    protected Actor target; //The target of the Actor's attack
    protected boolean fleeing = false; //Whether or not the Actor is successfully fleeing (don't fuck with this unless during combat)
    protected boolean attacking = false;
    
    protected Actor(Handler handler, float x, float y, int width, int height, String name, Weapon weapon,
            int level, int hitpoints, int mana, int exp, int strength, int dexterity, int wisdom, int intelligence,
            int luck, int defense, int agility){
        super(handler, x, y, width, height);
        this.name = name;
        this.weapon = weapon;
        this.level = level;
        this.hitpoints = hitpoints;
        this.maxHP = hitpoints;
        this.mana = mana;
        this.maxMP = mana;
        this.exp = exp;
        this.strength = strength;
        this.dexterity = dexterity;
        this.wisdom = wisdom;
        this.intelligence = intelligence;
        this.luck = luck;
        this.defense = defense;
        this.agility = agility;
        
        //Standard initializations
        effects = new HashMap<StatusEffect, Integer>();
    }
    
    //COMBAT METHODS
    /**
     * Actor attack method; calls methods for calculating damage dealt and received, as well as the
     * dealDamage method itself
     * @param target The Actor being targeted
     */
    public void attack(Actor target){
        int damage;
        UITextBox.resetBAOS();
        System.out.println(name + " prepares to attack...\n");
        
        attacking = true;
        handler.getCombat().animationDelay();
        attacking = false;
        
        //If the attack misses, return; otherwise, calculate initial damage dealt and then apply defense modifiers
        if (!attackHit()){
            System.out.println(name + " missed!");
            return;
        }
        
        System.out.println("...The attack hit!");
        
        
        damage = calcAttackDamage();
        
        damage = target.calcDamageReceived(this, damage, weapon.getType(), weapon.getEffect());
        
        //TODO: Implement status effects
        
        //If the attack wasn't evaded or completely blocked, deal the damage to the target
        if (damage > 0){
            System.out.println(target.name + " took " + damage + " damage!");
            target.dealDamage(damage);
        }
        else
            System.out.println("..." + target.getName() + " blocked the attack!");
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
        else if (25 <= strength && strength < 50)
            damage = (int) (baseDamage * (strength / 10.0) + (baseDamage * 2.4));
        else if (strength > 50)
            damage = (int) (baseDamage * (strength / 20.0) + (baseDamage * 4.9));
        
        //If the attack was critical, multiply the damage by 2
        if (attackCrit()){
            System.out.println("It was a critical hit!");
            damage *= 1.5;
        }
        
        return damage;
    }
    
    /**
     * Calculates total damage received by Actor by applying defense modifiers to damage dealt. Also
     * applies any status effects accompanied by attack.
     * @param attacker The Actor attacking this one
     * @param damage The amount of damage dealt, before applying defense modifiers
     * @param type The type of damage dealt
     * @param effect The status effect to be inflicted
     * @return The total damage received by the Actor
     */
    public int calcDamageReceived(Actor attacker, int damage, DamageType type, StatusEffect effect){
        /*
        Defense doesn't affect damage reduction directly; increasing defense increases damage type defenses
        (in addition to max HP), which in turn are used in damage reduction calculations.
        
        Each defense type starts at 50 and has a maximum value of 1000, at which point 100% of that
        type of damage is blocked. Damage calculations are done as follows: damage *= (1 - ([defense_type] / 1000)).
        */
        
        double damageBuffer = 0;
        
        switch (type){
            case SLASH:
                damageBuffer = 1 - (slashDef * .001);
                
                //If the Actor is currently using Dragon Skin, apply recoil damage to the attacker
                if (effects.containsKey(StatusEffect.DRAGON_SKIN) && attacker != null)
                    DragonSkin.dragonDamage((PlayableActor) this, attacker);
                break;
            case STAB:
                damageBuffer = 1 - (stabDef * .001);
                
                if (effects.containsKey(StatusEffect.DRAGON_SKIN) && attacker != null)
                    DragonSkin.dragonDamage((PlayableActor) this, attacker);
                break;
            case CRUSH:
                damageBuffer = 1 - (crushDef * .001);
                
                if (effects.containsKey(StatusEffect.DRAGON_SKIN) && attacker != null)
                    DragonSkin.dragonDamage((PlayableActor) this, attacker);
                break;
            case PIERCE:
                damageBuffer = 1 - (pierceDef * .001);
                
                if (effects.containsKey(StatusEffect.DRAGON_SKIN) && attacker != null)
                    DragonSkin.dragonDamage((PlayableActor) this, attacker);
                break;
            case MAGIC:
                damageBuffer = 1 - (magicDef * .001);
                
                break;
            case CHAOS:
                damageBuffer = 1 - (chaosDef * .001);
                
                break;
            case FIRE:
                damageBuffer = 1 - (fireDef * .001);
                
                break;
            case ICE:
                damageBuffer = 1 - (iceDef * .001);
                
                break;
            case EARTH:
                damageBuffer = 1 - (earthDef * .001);
                
                break;
            case LIGHTNING:
                damageBuffer = 1 - (lightningDef * .001);
                
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
        
        damage *= damageBuffer;
        
        //If there is an applicable status effect, run it against Actor's resistances and apply it
        if (effect != StatusEffect.NONE)
            statusHit(effect);
        
        return damage;
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
        of 5, base chance to hit will be 65%. Dexterity efficacy gradually diminishes as stat level
        increases; by level 25, it's half as effective, and by level 50, it's no longer a factor at
        all, and chances of missing remain at a constant 4%.
        */
        if (dexterity < 25){
            if (accuracy <= 40 - dexterity)
                hit = false;
            else
                hit = true;
        }
        else if (25 <= dexterity && dexterity < 50){
            if (accuracy <= 28 - (dexterity / 2))
                hit = false;
            else
                hit = true;
        }
        else if (dexterity >= 50){
            if (accuracy <= 4)
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
        int chance = dieRoll.nextInt(1000); //Roll for effect chance
        
        switch(effect){
            case POISON:
                //If the Actor is already poisoned, do nothing and return
                if (effects.containsKey(StatusEffect.POISON))
                    return;
                
                if (chance <= 500 - poisonRes)
                    effects.put(effect, StatusEffect.PERSISTENT);
                System.out.println(name + " has been poisoned!");
                break;
            case TOXIC:
                //If the Actor is already toxined, do nothing and return
                if (effects.containsKey(StatusEffect.TOXIC))
                    return;
                
                if (chance <= 500 - poisonRes)
                    effects.put(effect, StatusEffect.PERSISTENT);
                System.out.println(name + " has been toxined!");
                break;
            case STUN:
                //If the Actor is already stunned, do nothing and return
                if (effects.containsKey(StatusEffect.STUN))
                    return;
                
                if (chance <= 500 - stunRes)
                    effects.put(effect, StatusEffect.PERSISTENT);
                System.out.println(name + " has been stunned!");
                break;
            case FREEZE:
                //If the Actor is already frozen, do nothing and return
                if (effects.containsKey(StatusEffect.FREEZE))
                    return;
                
                if (chance <= 500 - freezeRes)
                    effects.put(effect, StatusEffect.PERSISTENT);
                System.out.println(name + " has been frozen!");
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
    }
    
    /**
     * Takes calculated damage and subtracts it from Actor's hitpoints
     * @param damage The amount of damage dealt
     */
    public void dealDamage(int damage){
        hitpoints -= damage; //Subract damage from Actor's hitpoints
        
        //If the Actor's hitpoints fall to or below 0, the Actor is dead
        if (hitpoints <= 0){
            hitpoints = 0;
            alive = false;
        }
    }
    
    /**
     * Restores the Actor's hitpoints by a given amount
     * @param restore The amount of hitpoints the Actor is to recover
     */
    public void heal(int restore){
        //If the Actor has Dragon Skin active, output a message and return
        if (effects.containsKey(StatusEffect.DRAGON_SKIN)){
            System.out.println(name + "'s Dragon Skin prevents restorative items/spells!");
            return;
        }
        
        hitpoints += restore; //Heal the player by the set amount of hitpoints
        
        System.out.println(name + " recovered " + restore + " hitpoints!");
        
        //If the Actor's hitpoints exceed the Actor's maxHP, reset them to be equal
        if (hitpoints >= maxHP)
            hitpoints = maxHP;
    }
    
    /**
     * Subtracts from the Actor's mana as a result of using a spell
     * @param spellMana The amount of mana the used spell takes
     */
    public void useMana(int spellMana){
        mana -= spellMana; //Subtract the amount of mana the spell uses from the Actor's current mana
        
        //If the Actor's mana falls to or below 0 (which should never happen), reset the mana to 0
        if (mana <= 0)
            mana = 0;
    }
    
    /**
     * Inflicts the Actor with a given status effect
     * @param effect The status effect to be inflicted
     * @param expires The number of turns after which the effect expires
     * @expires
     */
    public void addEffect(StatusEffect effect, int expires){
        //If the Actor isn't already afflicted by the status effect, apply it; otherwise, do nothing
        if (!effects.containsKey(effect))
            effects.put(effect, expires);
    }
    
    /**
     * Removes a given status effect from the Actor
     * @param effect The status effect to be removed
     */
    public void removeEffect(StatusEffect effect){
        //Only remove the effect if the Actor is currently afflicted with it; if not, do nothing
        if (effects.containsKey(effect)){
            effects.remove(effect);
            
            //If the effect modifies Actor stats, iterate through the list of modified stats and revert them to normal
            for (int i = 0; i < effect.getStats().length; i++)
                modifyStat(effect.getStats()[i], effect.getModifiers()[i]);
        }
    }
    
    /**
     * Updates the Actor's temporary status effects, having each one take effect and removing the ones that have expired
     * @param turn The number of the current turn
     */
    public void updateStatus(int turn){
        Iterator it = effects.entrySet().iterator(); //Create an Iterator for running through the HashMap
        
        //Run through the HashMap of status effects
        while (it.hasNext()){
            HashMap.Entry entry = (HashMap.Entry) it.next(); //Get the next entry
            StatusEffect effect = (StatusEffect) entry.getKey(); //Store the status effect in question
            int expires = (int) entry.getValue(); //Store the turn on which the effect expires
            
            //If the current turn is the turn on which the effect expires, remove the effect from the Actor; otherwise, have it take effect
            if (expires == turn)
                removeEffect(effect);
            else
                effect.effect(this);
            
            Combat.delay(); //Run the Combat delay
            
            //it.remove(); //Avoids a ConcurrentModificationException... whatever the fuck that is... [Commented out for now because it threw the exact error it was meant to avoid, and I'm really not sure what it does]
        }
    }
    
    /**
     * Runs the batch of methods in the Actor's action buffer for combat
     */
    public void takeTurn(){
        //Only run the batch of methods if it isn't null
        if (action != null)
            action.run();
    }
    
    /**
     * Actor flee method; test against agility to see if flee attempt is successful.
     */
    public void flee(){
        int chance = dieRoll.nextInt(100); //Roll for chance to flee
        /*
        Determines flee success by a combination of die roll and agility. With base agility level of
        5, initial chance to flee will be 65%. Agility efficacy gradually diminishes as stat level increases;
        by level 25, it's half as effective, and by level 50, it's no longer a factor at all, and chances
        of failing remain at a constant 4%.
        */
        
        if (agility < 25){
            if (chance <= 40 - agility)
                fleeing = false;
            else
                fleeing = true;
        }
        else if (25 <= agility && agility < 50){
            if (chance <= 28 - (agility / 2))
                fleeing = false;
            else
                fleeing = true;
        }
        else if (agility >= 50){
            if (chance <= 4)
                fleeing = false;
            else
                fleeing = true;
        }
        
        UITextBox.resetBAOS();
        
        if (fleeing)
            System.out.println("You got away!");
        else
            System.out.println("You couldn't get away!");
    }
    
    //MISCELLANEOUS METHODS
    
    /**
     * Saves this version of an Actor to an object file
     * @param actor The Actor to be saved
     * @param character The character of the actor
     */
    protected void save(Actor actor, Characters character){
        try{
            fileOut = new FileOutputStream("SaveFiles/Actors/" + character.getValue());
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
            fileIn = new FileInputStream("SaveFiles/Actors/" + name);
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
    
    /**
     * Returns the current animation frame so that it can be rendered
     * @return A BufferedImage containing the current frame of the animation
     */
    protected abstract BufferedImage getCurrentAnimationFrame();
    
    /**
     * Increases or decreases a given stat by a given amount
     * @param stat The name of the stat to be modified
     * @param modify The amount of points by which the stat is to be modified
     */
    protected abstract void modifyStat(Stat stat, int modify);
    
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
    
    public int getExp() {
        return exp;
    }
    
    public void setExp(int exp) {
        this.exp = exp;
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
    
    public int getAgility() {
        return agility;
    }
    
    public void setAgility(int agility) {
        this.agility = agility;
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
    
    public int getChaosDef() {
        return chaosDef;
    }
    
    public void setChaosDef(int chaosDef) {
        this.chaosDef = chaosDef;
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
    
    public Runnable getAction() {
        return action;
    }
    
    public void setAction(Runnable action) {
        this.action = action;
    }
    
    public HashMap<StatusEffect, Integer> getEffects() {
        return effects;
    }
    
    public void setEffects(HashMap<StatusEffect, Integer> effects) {
        this.effects = effects;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public boolean isFleeing() {
        return fleeing;
    }
    
    public void setFleeing(boolean fleeing) {
        this.fleeing = fleeing;
    }
    
    public boolean isAttacking() {
        return attacking;
    }
    
    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
}