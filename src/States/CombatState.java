/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Main.Handler;
import UI.UIManager;
import Worlds.World;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public class CombatState extends State{
    private World world; //The world
    private UIManager uiManager; //The UI Manager
    
    public CombatState(Handler handler){
        super(handler);
        world = new World(handler, "Resources/Worlds/World.txt");
        handler.setWorld(world);
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