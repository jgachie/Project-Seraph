/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors.PlayableActors;

import Entities.Creatures.Actors.Actor;
import Enums.Characters;
import Enums.States;
import Graphics.Animation;
import Graphics.Assets;
import Inventory.Inventory;
import Items.Equipment.Weapon;
import Items.UsableItems.Potion;
import Main.Handler;
import States.CombatState;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import States.State;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author Soup
 */
public class Player extends PlayableActor{
    //Declare animations (must be static for serialization)
    private static Animation walkDown, walkUp, walkLeft, walkRight;
    private static Animation runDown, runUp, runLeft, runRight;
    private static Animation fightUp, fightDown, fightLeft, fightRight;
    private static Animation attack, cast, fight;
    
    private final Inventory inv; //The Player's inventory
    
    public Player(Handler handler, float x, float y, String name){
        super(handler, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT, name, Characters.SARIEL,
                Weapon.broadsword, 1, 100, 100, 100, 0, 5, 5, 99, 99, 5, 5, 5, 5, new ArrayList<PlayableActor>());
        
        //Set bounding box coordinates (relative to top-left corner of Player entity) and width/height
        bounds.x = 8;
        bounds.y = 12;
        bounds.width = 16;
        bounds.height = 28;
        
        //Initialize animations
        walkDown = new Animation(150, false, Assets.playerDown);
        walkUp = new Animation(150, false, Assets.playerUp);
        walkLeft = new Animation(150, false, Assets.playerLeft);
        walkRight = new Animation(150, false, Assets.playerRight);
        
        attack = new Animation(150, true, Assets.playerAttackRight);
        cast = new Animation(150, true, Assets.playerCastRight);
        fight = new Animation(200, false, Assets.playerFightRight);
        
        //Initialize other shit
        inv = new Inventory(handler);
        inv.addItem(new Potion());
        inv.addItem(Weapon.broadsword);
        party.add(this); //Add the player to the party
    }
    
    /**
     * Adds a given PlayableActor to the Player's party
     * @param actor The PlayableActor being added to the party
     */
    public void partyAdd(PlayableActor actor){
        //Return if the party is full, the Actor is already in the party, or the Actor is invalid
        if (party.size() >= 4 || party.contains(actor) || actor instanceof Player)
            return;
        
        //Add the PlayableActor to the party
        party.add(actor);
    }
    
    /**
     * Removes a given PlayableActor from the Player's party.
     *
     * Note: As the Player must remain in the party at all times, attempting to remove the Player will
     * cause the method to return without doing anything
     * @param actor The PlayableActor being added to the party
     */
    public void partyRemove(PlayableActor actor){
        //Return if the party is empty (save for the Player), the Actor isn't currently in the party, or the Actor is the Player himself
        if (party.size() <= 1 || !party.contains(actor) || actor instanceof Player)
            return;
        
        //Remove the PlayableActor from the party
        party.remove(actor);
    }
    
    /**
     * Swaps the positions of two party members
     *
     * Note: May remove this method later. Party positions really only determine the order in which
     * each party member appears on the screen during combat, so this method has no practical use as
     * of yet
     * @param actor1 The first party member to be swapped
     * @param actor2 The second party member to be swapped
     */
    public void partySwap(PlayableActor actor1, PlayableActor actor2){
        //If either one of the Actors aren't in the party, return
        if (!party.contains(actor1) || !party.contains(actor2))
            return;
        
        Collections.swap(party, party.indexOf(actor1), party.indexOf(actor2));
    }
    
    @Override
    public void tick(){
        inv.tick();
        
        //Combat ticking rules
        if (State.getState() instanceof CombatState){
            if (attacking){
                if (!attack.isCompleted())
                    attack.tick();
                else{
                    attack.setCompleted(false); //Reset attack animation before notifying the combat thread
                    synchronized(handler.getCombat()){
                        handler.getCombat().notifyAll();
                    }
                }
            }
            else if (casting)
                cast.tick();
            //If the Player is idling, tick the fight animation
            else{
                fight.tick();
                return;
            }
        }
        
        //If enough steps have been taken, trigger the encounter and initiate combat
        if (steps >= handler.getEncounter().getSteps()){
            steps = 0; //Reset the number of steps
            handler.getGame().setState(States.COMBAT); //Start combat
            handler.setEncounter(null); //After combat finishes, reset the encounter
        }
        
        //Tick animations
        walkDown.tick();
        walkUp.tick();
        walkLeft.tick();
        walkRight.tick();
        
        //Movements
        getInput(); //Get Player input
        move(); //Apply input and move Player entity
        handler.getGameCamera().centerOnEntity(this); //Center camera on Player entity
    }
    
    /**
     * Manages input for Player entity
     */
    private void getInput(){
        //Reset movement buffers
        xMove = 0;
        yMove = 0;
        
        //Movement controls; buffer is changed in increments defined by speed
        if (handler.getKeyManager().up)
            yMove = -speed;
        if (handler.getKeyManager().down)
            yMove = speed;
        if (handler.getKeyManager().left)
            xMove = -speed;
        if (handler.getKeyManager().right)
            xMove = speed;
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER))
            handler.getGame().setState(States.PAUSE);
    }
    
    @Override
    public void render(Graphics g){
        inv.render(g);
        
        BufferedImage frame = getCurrentAnimationFrame();
        
        //If the Player is in combat, render normally, but without the camera offsets
        if (State.getState() instanceof CombatState){
            g.drawImage(frame, (int) x, (int) y, frame.getWidth() * 2, frame.getHeight() * 2, null);
            return;
        }
        
        //Subtract offset from position to focus camera on Player
        g.drawImage(frame, (int) (x - handler.getGameCamera().getXOffset()),
                (int) (y - handler.getGameCamera().getYOffset()), frame.getWidth() * 2, frame.getHeight() * 2, null);
    }
    
    @Override
    protected BufferedImage getCurrentAnimationFrame(){
        //Combat rendering rules
        if (State.getState() instanceof CombatState){
            if (attacking){
                if (!attack.isCompleted())
                    return attack.getCurrentFrame();
            }
            else if (casting){
                //If the animation has completed but the Player is still casting, return the last frame
                if (cast.isCompleted())
                    return cast.getLastFrame();
                
                return cast.getCurrentFrame();
            }
            
            //If the Player is idling, return the current frame of the fight animation
            return fight.getCurrentFrame();
        }
        
        if (xMove < 0)
            return walkLeft.getCurrentFrame();
        else if (xMove > 0)
            return walkRight.getCurrentFrame();
        else if (yMove < 0)
            return walkUp.getCurrentFrame();
        else if (yMove > 0)
            return walkDown.getCurrentFrame();
        else{
            return Assets.player;
        }
    }
    
    /**
     * Player-specific load method; calls the load supermethod with a specific filename
     * @param handler A handler object to replace the one that wasn't serialized
     * @return The loaded Player object
     */
    public static Player load(Handler handler){
        Player player = (Player) Actor.load("Sariel");
        player.resetHandler(handler);
        return player;
    }
    
    /**
     * Resets handlers for all necessary objects after loading Player object from save file
     * @param handler The handler object
     */
    private void resetHandler(Handler handler){
        this.handler = handler;
        inv.setHandler(handler);
    }
    
    @Override
    public void resetAnimations(){
        cast.setCompleted(false);
        attack.setCompleted(false);
    }
    
    //GETTERS/SETTERS
    
    public Inventory getInventory() {
        return inv;
    }
}