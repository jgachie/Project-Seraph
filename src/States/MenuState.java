/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package States;

import Graphics.Assets;
import Main.Handler;
import UI.ClickListener;
import UI.UIImageButton;
import UI.UIManager;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Soup
 */
public class MenuState extends State{
    private UIManager manager; //The UI Manager
    
    public MenuState(Handler handler){
        super(handler);
        manager = new UIManager(handler); //Initialize the UI Manager
        handler.getMouseManager().setUIManager(manager); //Feed the manager through to the Mouse Manager to facilitate click and move events
        
        /*
        Add the start button to the UI Manager; the UIImageButton requires a ClickListener, so we'll
        create a subclass within the method call to satisfy it. Define the onClick() method to do whatever
        clicking on the button is supposed to do.
        */
        manager.addObject(new UIImageButton(200, 200, 128, 64, Assets.btn_start, new ClickListener(){
            @Override
            public void onClick() {
                handler.getMouseManager().setUIManager(null); //Dispose of the menu state's UI Manager
                State.setState(handler.getGame().getState("Game")); //When the button is clicked, start the game
            }
        }));
    }
    
    @Override
    public void tick() {
        manager.tick();
    }
    
    @Override
    public void render(Graphics g) {
        manager.render(g);
    }
    
}
