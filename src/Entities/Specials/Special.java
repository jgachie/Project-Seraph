/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Specials;

import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Entity;
import Graphics.Animation;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author Soup
 */
public abstract class Special extends Entity{
    protected static Random dieRoll = new Random(); //A Random object for determining various outcomes
    public static final float DEFAULT_SPEED = 5.0f; //Special entity default speed
    public static final int DEFAULT_ANIMATION_SPEED = 100; //Special entity default animation speed
    public static final int DEFAULT_SPECIAL_WIDTH = 32, DEFAULT_SPECIAL_HEIGHT = 32; //Special entity default width/height
    
    protected float speed; //How fast the special entity moves
    protected float xMove, yMove; //Movement buffers; holds amount of pixels moved and is added to position later to show movement
    protected float targetX, targetY; //Coordinates of the special's target
    protected boolean hit; //Whether the special entity has hit its target yet
    protected PlayableActor user; //The Actor using the special
    protected Actor target; //The Actor being targeted by the special
    
    protected final String name; //The name of the special entity
    protected final String desc; //The special entity's description
    protected final int pointReq; //The amount of action points (mana, skillpoints, etc) required to use the special entity
    protected final int baseChance; //The base chance of the special entity succeeding
    protected final int critChance; //The base chance of the special entity getting a critical hit
    protected int minDamage, maxDamage; //The lowest and highest amounts of base damage the special entity can deal
    protected final boolean friendly; //Whether the special is to be used on the Player's party or the enemy's
    protected final boolean multi; //Whether the special is used on multiple targets or just one
    
    public Special(float x, float y, int width, int height, String name, String desc, int pointReq,
            int baseChance, int critChance, int minDamage, int maxDamage, boolean friendly, boolean multi){
        super(null, x, y, width, height);
        
        this.speed = DEFAULT_SPEED;
        this.xMove = 0;
        this.yMove = 0;
        this.name = name;
        this.desc = desc;
        this.pointReq = pointReq;
        this.baseChance = baseChance;
        this.critChance = critChance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.friendly = friendly;
        this.multi = multi;
        this.hit = false;
    }
    
    /**
     * Facilitates special entity movement
     */
    public void move(){
        //Check for collisions before moving
        if (!checkEntityCollisions(DEFAULT_SPEED, 0f))
            x += DEFAULT_SPEED; //If no collision, move horizontally
        else
            hit = true; //If there is a collision, the entity has hit its target, so set hit to true
        if (!checkEntityCollisions(0f, DEFAULT_SPEED))
            moveY(); //If no collision, move vertically
        else
            hit = true; //If there is a collision, the entity has hit its target, so set hit to true
    }
    
    /**
     * Facilitates horizontal special entity movement
     */
    public void moveX(){
        x += xMove; //Add the value of the movement buffer to the entity's position
    }
    
    /**
     * Facilitates vertical special entity movement
     */
    public void moveY(){
        y += yMove; //Add the value of the movement buffer to the entity's position
    }
    
    /**
     * Returns the current animation frame so that it can be rendered
     * @return A BufferedImage containing the current frame of the animation
     */
    protected abstract BufferedImage getCurrentAnimationFrame();
    
    /**
     * Resets various boolean values to false after special animation finishes completely
     * @param animations All of the animations used for the special, which need to have their "complete" boolean reset to false
     */
    protected void resetValues(Animation... animations){
        active = false;
        hit = false;
        
        for (Animation anime : animations)
            anime.setCompleted(false);
    }
    
    public String getName() {
        return name;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public int getPointReq() {
        return pointReq;
    }
    
    public int getBaseChance() {
        return baseChance;
    }
    
    public int getCritChance() {
        return critChance;
    }
    
    public int getMinDamage() {
        return minDamage;
    }
    
    public int getMaxDamage() {
        return maxDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getxMove() {
        return xMove;
    }

    public void setxMove(float xMove) {
        this.xMove = xMove;
    }

    public float getyMove() {
        return yMove;
    }

    public void setyMove(float yMove) {
        this.yMove = yMove;
    }
    
    public boolean getFriendly() {
        return friendly;
    }
    
    public boolean getMulti(){
        return multi;
    }
}