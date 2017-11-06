/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Combat.Combat;
import Combat.Encounter;
import Entities.Creatures.Actors.Actor;
import Entities.Creatures.Actors.PlayableActors.Player;
import Main.Handler;
import UI.UIManager;
import Worlds.World;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class CombatState extends State{
    private World world; //The world
    private UIManager uiManager; //The UI Manager
    private Player player; //The Player
    
    public CombatState(Handler handler){
        super(handler);
        player = Player.load(handler);
        new Encounter(handler);
        Combat combat = new Combat(handler, player.getParty(), handler.getEncounter().getParty());
        world = new World(handler, "Resources/Worlds/World.txt", player.getParty(), handler.getEncounter().getParty(), combat);
        handler.setWorld(world); //Commented out for now; no need to fetch combat world from anywhere else, and it'll only get in the way of getting the main game world
        combat.start();
    }

    @Override
    public void tick() {
        world.tick();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
    }
}