/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Entities.Creatures.Actors.PlayableActor.Player;
import Entities.Statics.Tree;
import Main.Handler;
import Worlds.World;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public class GameState extends State{
    private World world; //The world
    
    public GameState(Handler handler){
        super(handler);
        world = new World(handler, "Resources/Worlds/World.txt");
        handler.setWorld(world);
    }

    @Override
    public void tick(){
        //Switch to menu state when scroll button is pressed
        if (handler.getMouseManager().isMiddlePressed())
            State.setState(handler.getGame().getState("Menu"));
        
        world.tick();
    }

    @Override
    public void render(Graphics g){
        world.render(g);
    }
}