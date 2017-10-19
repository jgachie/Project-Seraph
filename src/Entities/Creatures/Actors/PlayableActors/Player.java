/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Entities.Creatures.Actors.PlayableActors;

import Entities.Creatures.Actors.Actor;
import Enums.Characters;
import Graphics.Animation;
import Graphics.Assets;
import Inventory.Inventory;
import Items.Equipment.Weapon;
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
    
    private Inventory inv; //The Player's inventory
    
    public Player(Handler handler, float x, float y, String name){
        super(handler, x, y, DEFAULT_CREATURE_WIDTH, DEFAULT_CREATURE_HEIGHT, name, Characters.SARIEL,
                Weapon.broadsword, 1, 100, 100, 100, 0, 5, 5, 5, 5, 5, 5, 5, 5, new ArrayList<PlayableActor>());
        
        //Set bounding box coordinates (relative to top-left corner of Player entity) and width/height
        bounds.x = 8;
        bounds.y = 12;
        bounds.width = 16;
        bounds.height = 28;
        
        //Initialize animations
        walkDown = new Animation(150, Assets.player_down);
        walkUp = new Animation(150, Assets.player_up);
        walkLeft = new Animation(150, Assets.player_left);
        walkRight = new Animation(150, Assets.player_right);
        
        //Initialize other shit
        inv = new Inventory(handler);
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
        //If the Player is currently engaged in combat, don't take in input for movement; just do nothing and return
        if (State.getState() instanceof CombatState)
            return;
        
        //If enough steps have been taken, trigger the encounter and initiate combat
        if (steps >= handler.getEncounter().getSteps()){
            steps = 0; //Reset the number of steps
            handler.getGame().setState("Combat"); //Start combat
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
        //inv.tick(); //Commented out for now; don't really know when (or even why) inventory should be ticked
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
            handler.getGame().setState("Pause");
    }
    
    @Override
    public void render(Graphics g){
        //If the Player is currently engaged in combat, render the basic Player asset at a fixed position (so no need for camera offsets) and return
        if (State.getState() instanceof CombatState){
            g.drawImage(Assets.player, (int) (x), (int) (y), width, height, null);
            return;
        }
            
        //Subtract offset from position to focus camera on Player
        g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getGameCamera().getXOffset()),
                (int) (y - handler.getGameCamera().getYOffset()), width, height, null);
        //inv.render(g); //Commented out for now; don't really know when (or even why) inventory should be rendered
    }
    
    private BufferedImage getCurrentAnimationFrame(){
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
        player.setHandler(handler);
        return player;
    }
    
    /**
     * Sets handlers for all necessary objects
     * @param handler The handler object
     */
    private void setHandler(Handler handler){
        this.handler = handler;
        inv.setHandler(handler);
    }
}