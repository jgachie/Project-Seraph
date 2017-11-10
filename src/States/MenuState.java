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
import java.awt.Graphics;
import java.io.Serializable;

/**
 *
 * @author Soup
 */
public class MenuState extends State{
    private UIManager uiManager; //The UI Manager
    
    public MenuState(Handler handler){
        super(handler);
        uiManager = new UIManager(handler); //Initialize the UI Manager
        handler.getMouseManager().setUIManager(uiManager); //Feed the manager through to the Mouse Manager to facilitate click and move events
        
        /*
        Add the start button to the UI Manager; the UIImageButton requires a ClickListener, so we'll
        create a subclass within the method call to satisfy it. Define the onClick() method to do whatever
        clicking on the button is supposed to do.
        */
        uiManager.addObject(new UIImageButton("Start", handler.getWidth() / 2 - 64, handler.getHeight() / 2 - 32, 128, 64, true, Assets.btn, (ClickListener & Serializable)() -> {
            handler.getGame().setState("Game"); //When the button is clicked, start the game
        }));
    }
    
    @Override
    public void tick() {
        uiManager.tick();
    }
    
    @Override
    public void render(Graphics g) {
        uiManager.render(g);
    }
    
    public UIManager getUIManager(){
        return uiManager;
    }
    
    public void setUIManager(UIManager uiManager){
        this.uiManager = uiManager;
    }
}