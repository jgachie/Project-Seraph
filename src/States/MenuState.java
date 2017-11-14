/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package States;

import Enums.States;
import Graphics.Assets;
import Main.Game;
import Main.Handler;
import UI.ClickListener;
import UI.UIImageButton;
import UI.UIManager;
import UI.UITextBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class MenuState extends State{
    private UIManager uiManager; //The UI Manager
    private ArrayList<File> saveFiles; //An ArrayList containing the Player's game save files
    private UIImageButton startButton, loadButton;
    private boolean loadMenu = false; //A boolean telling whether to display the load menu or not
    
    public MenuState(Handler handler){
        super(handler);
        saveFiles = new ArrayList<File>(); //Initialize the save files array
        uiManager = new UIManager(handler); //Initialize the UI Manager
        handler.getMouseManager().setUIManager(uiManager); //Feed the manager through to the Mouse Manager to facilitate click and move events
        
        /*
        Add the start button to the UI Manager; the UIImageButton requires a ClickListener, so we'll
        create a subclass within the method call to satisfy it. Define the onClick() method to do whatever
        clicking on the button is supposed to do.
        */
        uiManager.addObject(new UITextBox(0f, 0f, handler.getWidth(), handler.getHeight(), true, Assets.menu)); //Add a textbox to display as the menu screen
        startButton = new UIImageButton("Start", handler.getWidth() / 2 - 160, handler.getHeight() / 2 - 32, 128, 64, true, Assets.btn, (ClickListener & Serializable)() -> {
            handler.getGame().setState(States.GAME); //When the button is clicked, start the game
        });
        loadButton = new UIImageButton("Load Game", handler.getWidth() / 2 + 32, handler.getHeight() / 2 - 32, 128, 64, true, Assets.btn, (ClickListener & Serializable) () -> {
            //Clear all of the buttons from the UI manager
            for (int i = 1; i < uiManager.getObjects().size();)
                uiManager.removeObject(i);
            loadMenu = true;
            
            for (int i = 0; i < 5; i++){
                int index = i;
                saveFiles.add(i, new File("SaveFiles/Saves/save" + i));
                
                uiManager.addObject(new UIImageButton("Load", handler.getWidth() / 2, 150 + (index * 70), 128, 64, true, Assets.btn, (ClickListener & Serializable)() -> {
                    Game game = handler.getGame().load(saveFiles.get(index));
                    game.setThread(new Thread());
                    game.start();
                    game.setState(States.GAME);
                    handler.setGame(game);
                }));
                    uiManager.addObject(new UIImageButton("Delete", handler.getWidth() / 2 + 150, 150 + (index * 70), 128, 64, true, Assets.btn, (ClickListener & Serializable)() -> {
                        saveFiles.get(index).delete();
                    }));
            }
            
            uiManager.addObject(new UIImageButton("Back", 100, 100, 128, 64, true, Assets.btn, (ClickListener & Serializable) () -> {
                //Clear all of the buttons from the UI manager
                for (int i = 1; i < uiManager.getObjects().size();)
                    uiManager.removeObject(i);
                
                loadMenu = false;
                
                //Add the start and load buttons back to the UI manager
                uiManager.addObject(startButton);
                uiManager.addObject(loadButton);
            }));
        });
        
        uiManager.addObject(startButton);
        uiManager.addObject(loadButton);
    }
    
    @Override
    public void tick() {
        uiManager.tick();
    }
    
    @Override
    public void render(Graphics g) {
        uiManager.render(g);
        
        g.setFont(new Font("Rockwell", Font.PLAIN, 60));
        g.setColor(Color.WHITE);
        
        g.drawString("PROJECT SARIEL", handler.getWidth() / 2 - g.getFontMetrics().stringWidth("PROJECT SARIEL") / 2, g.getFontMetrics().getHeight());
        
        if (loadMenu){
            g.setFont(new Font("Rockwell", Font.PLAIN, 40));
            g.drawString("Load", handler.getWidth() / 2 - g.getFontMetrics().stringWidth("Load") / 2, g.getFontMetrics().getHeight());
            
            
            g.setFont(new Font("Rockwell", Font.BOLD, 20));
            
            for (int i = 0; i < saveFiles.size(); i++){
                if (saveFiles.get(i).exists())
                    g.drawString("Slot " + (i + 1) + ": (Save File)", 30, 150 + 32 + (i * 70));
                else
                    g.drawString("Slot " + (i + 1) + ": (Empty)", 30, 150 + 32 + (i * 70));
            }
        }
    }
    
    public UIManager getUIManager(){
        return uiManager;
    }
    
    public void setUIManager(UIManager uiManager){
        this.uiManager = uiManager;
    }
}