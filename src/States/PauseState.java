/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package States;

import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Enums.Stat;
import Enums.States;
import Graphics.Assets;
import Items.Item;
import Main.Game;
import Main.Handler;
import UI.ClickListener;
import UI.UIImageButton;
import UI.UIManager;
import UI.UIPointer;
import UI.UITextBox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class PauseState extends State{
    private transient static FileInputStream fileIn;
    private transient static FileOutputStream fileOut;
    
    private UIManager uiManager; //The UI Manager
    private UIPointer pointer; //A pointer to be used in the inventory submenu
    private ArrayList<File> saveFiles; //An ArrayList containing the Player's game save files
    private String submenu = "Pause"; //The currently displayed submenu; defaults to "Pause", which is the basic pause screen
    private boolean menuSwitch = true; //Whether or not the current menu needs to switched
    private PlayableActor actor; //The Actor whose stats are currently being dispalyed
    
    public PauseState(Handler handler) {
        super(handler);
        saveFiles = new ArrayList<File>(); //Initialize the save file array
        uiManager = new UIManager(handler); //Initialize the UI Manager
        handler.getMouseManager().setUIManager(uiManager); //Feed the manager through to the Mouse Manager to facilitate click and move events
        
        uiManager.addObject(new UITextBox(0f, 0f, handler.getWidth(), handler.getHeight(), true, Assets.menu)); //Add a textbox to display as the menu screen
    }
    
    @Override
    public void tick() {
        UITextBox.resetBAOS(); //Clear the BAOS
        
        //When the enter key is pressed, switch back to the game
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && !submenu.equalsIgnoreCase("Level Up")){
            submenu = "Pause";
            menuSwitch = true;
            actor = null;
            handler.getGame().setState(States.GAME);
        }
        
        uiManager.tick();
        
        //If the submenu needs to be switched, switch it
        if (menuSwitch)
            switchSubmenu();
        
        //Set boundaries for the pointer so that the player can't just scroll it right off the page
        if (submenu.equals("Inventory")){
            if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && pointer.getY() + 30 < 720)
                pointer.setY(pointer.getY() + 30);
            else if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && pointer.getY() - 30 > 180)
                pointer.setY(pointer.getY() - 30);
        }
    }
    
    @Override
    public void render(Graphics g) {
        uiManager.render(g);
        
        g.setFont(new Font("Rockwell", Font.PLAIN, 60)); //Set the font for the screen title
        g.setColor(Color.WHITE); //Set the color
        
        g.drawString("PAUSE", handler.getWidth() / 2 - g.getFontMetrics().stringWidth("PAUSE") / 2, g.getFontMetrics().getHeight()); //Draw the screen title
        
        g.setFont(new Font("Rockwell", Font.PLAIN, 40)); //Set the font for the submenu heading
        
        //Draw the title of the submenu, provided it's not the "Pause" submenu
        if (!submenu.equals("Pause"))
            g.drawString(submenu, handler.getWidth() / 2 - g.getFontMetrics().stringWidth(submenu) / 2, 120);
        
        g.setFont(new Font("Rockwell", Font.BOLD, 20)); //Set the font for the submenu body text
        
        switch (submenu.toUpperCase()){
            case "PARTY":
                int col1 = (int) (handler.getWidth() * .5) + 35;
                int col2 = (int) (handler.getWidth() * .75);
                int fontHeight = g.getFontMetrics().getHeight();
                
                g.drawLine(handler.getWidth() / 2, 150, handler.getWidth() / 2, handler.getHeight() - 50); //Draw a dividing line down the center of the screen
                
                //If there is an actor set right now, display their stats; otherwise, draw the stat names and leave them empty
                if (actor != null){
                    //Column 1
                    g.drawString("Name: " + actor.getName(), col1, 160 + fontHeight);
                    g.drawString("HP: " + actor.getHitpoints() + '/' + actor.getMaxHP(), col1, 160 + fontHeight * 2);
                    g.drawString("EXP: " + actor.getExp() + '/' + actor.getExpCap(), col1, 160 + fontHeight * 3);
                    g.drawString("STR: " + actor.getStrength(), col1, 160 + fontHeight * 4);
                    g.drawString("WIS: " + actor.getWisdom(), col1, 160 + fontHeight * 5);
                    g.drawString("LUK: " + actor.getLuck(), col1, 160 + fontHeight * 6);
                    g.drawString("AGI: " + actor.getAgility(), col1, 160 + fontHeight * 7);
                    g.drawString("Weapon: " + actor.getWeapon().getName(), col1, 160 + fontHeight * 8);
                    
                    //Column 2
                    g.drawString("Level: " + actor.getLevel(), col2, 160 + fontHeight);
                    g.drawString("MP: " + actor.getMana() + '/' + actor.getMaxMP(), col2, 160 + fontHeight * 2);
                    g.drawString("SP: " + actor.getSkillpoints() + '/' + actor.getMaxSP(), col2, 160 + fontHeight * 3);
                    g.drawString("DEX: " + actor.getDexterity(), col2, 160 + fontHeight * 4);
                    g.drawString("INT: " + actor.getIntelligence(), col2, 160 + fontHeight * 5);
                    g.drawString("DEF: " + actor.getDefense(), col2, 160 + fontHeight * 6);
                    g.drawString("SKL: " + actor.getSkill(), col2, 160 + fontHeight * 7);
                }
                else{
                    //Column 1
                    g.drawString("Name: ", col1, 160 + fontHeight);
                    g.drawString("HP: ", col1, 160 + fontHeight * 2);
                    g.drawString("EXP: ", col1, 160 + fontHeight * 3);
                    g.drawString("STR: ", col1, 160 + fontHeight * 4);
                    g.drawString("WIS: ", col1, 160 + fontHeight * 5);
                    g.drawString("LUK: ", col1, 160 + fontHeight * 6);
                    g.drawString("AGI: ", col1, 160 + fontHeight * 7);
                    g.drawString("Weapon: ", col1, 160 + fontHeight * 8);
                    
                    //Column 2
                    g.drawString("Level: ", col2, 160 + fontHeight);
                    g.drawString("MP: ", col2, 160 + fontHeight * 2);
                    g.drawString("SP: ", col2, 160 + fontHeight * 3);
                    g.drawString("DEX: ", col2, 160 + fontHeight * 4);
                    g.drawString("INT: ", col2, 160 + fontHeight * 5);
                    g.drawString("DEF: ", col2, 160 + fontHeight * 6);
                    g.drawString("SKL: ", col2, 160 + fontHeight * 7);
                }
                break;
            case "INVENTORY":
                ArrayList<Item> inv = handler.getWorld().getEntityManager().getPlayer().getInventory().getInventory();
                
                
                g.drawLine(handler.getWidth() / 2, 150, handler.getWidth() / 2, handler.getHeight() - 50); //Draw a dividing line down the center of the screen
                
                int height = g.getFontMetrics().getHeight();
                
                for (int i = 0; i < inv.size(); i++)
                    g.drawString(inv.get(i).getName() + " x" + inv.get(i).getCount(), 50, 150 + height * (i + 2) + 6 * i);
                break;
            case "OPTIONS":
                break;
            case "SAVE/LOAD":
                for (int i = 0; i < saveFiles.size(); i++){
                    if (saveFiles.get(i).exists())
                        g.drawString("Slot " + (i + 1) + ": (Save File)", 30, 150 + 32 + (i * 70));
                    else
                        g.drawString("Slot " + (i + 1) + ": (Empty)", 30, 150 + 32 + (i * 70));
                }
                break;
            case "LEVEL UP":
                col1 = 35;
                col2 = (int) (handler.getWidth() * .25);
                fontHeight = g.getFontMetrics().getHeight(); //Set the font height;
                
                //Column 1
                g.drawString("Name: " + actor.getName(), col1, 160 + fontHeight);
                g.drawString("HP: " + actor.getHitpoints() + '/' + actor.getMaxHP(), col1, 160 + fontHeight * 2);
                g.drawString("STR: " + actor.getStrength(), col1, 160 + fontHeight * 3);
                g.drawString("WIS: " + actor.getWisdom(), col1, 160 + fontHeight * 4);
                g.drawString("LUK: " + actor.getLuck(), col1, 160 + fontHeight * 5);
                g.drawString("AGI: " + actor.getAgility(), col1, 160 + fontHeight * 6);
                
                //Column 2
                g.drawString("Level: " + actor.getLevel(), col2, 160 + fontHeight);
                g.drawString("MP: " + actor.getMana() + '/' + actor.getMaxMP(), col2, 160 + fontHeight * 2);
                g.drawString("SP: " + actor.getSkillpoints() + '/' + actor.getMaxSP(), col2, 160 + fontHeight * 3);
                g.drawString("DEX: " + actor.getDexterity(), col2, 160 + fontHeight * 4);
                g.drawString("INT: " + actor.getIntelligence(), col2, 160 + fontHeight * 5);
                g.drawString("DEF: " + actor.getDefense(), col2, 160 + fontHeight * 6);
                g.drawString("SKL: " + actor.getSkill(), col2, 160 + fontHeight * 7);
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
    }
    
    /**
     * Switches between the different submenus of the pause screen
     */
    private void switchSubmenu(){
        //Clear all of the buttons from the UI manager
        for (int i = 1; i < uiManager.getObjects().size();)
            uiManager.removeObject(i);
        
        //Switch to the current submenu
        switch (submenu.toUpperCase()){
            case "PAUSE":
                uiManager.addObject(new UIImageButton("Party", 50, 150, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Party";
                    menuSwitch = true;
                }));
                uiManager.addObject(new UIImageButton("Inventory", 50, 250, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Inventory";
                    menuSwitch = true;
                }));
                uiManager.addObject(new UIImageButton("Options", 50, 350, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Options";
                    menuSwitch = true;
                }));
                uiManager.addObject(new UIImageButton("Save/Load", 50, 450, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Save/Load";
                    menuSwitch = true;
                }));
                uiManager.addObject(new UIImageButton("Back", 50, 650, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Pause";
                    menuSwitch = true;
                    actor = null;
                    handler.getGame().setState(States.GAME);
                }));
                break;
            case "PARTY":
                ArrayList<PlayableActor> party = handler.getWorld().getEntityManager().getPlayer().getParty();
                
                for (int i = 0; i < party.size(); i++){
                    int index = i;
                    uiManager.addObject(new UIImageButton(party.get(i).getName(), 50, 150 + (100 * i), 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                        actor = party.get(index);
                    }));
                }
                
                uiManager.addObject(new UIImageButton("Back", 50, 150 + (100 * party.size()), 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Pause";
                    menuSwitch = true;
                    actor = null;
                }));
                break;
            case "INVENTORY":
                pointer = new UIPointer(5, 182, 38, 18, true);
                uiManager.addObject(pointer);
                uiManager.addObject(new UIImageButton("Back", 100, 100, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Pause";
                    menuSwitch = true;
                }));
                break;
            case "OPTIONS":
                break;
            case "SAVE/LOAD":
                uiManager.addObject(new UIImageButton("Back", 100, 100, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                    submenu = "Pause";
                    menuSwitch = true;
                }));
                
                for (int i = 0; i < 5; i++){
                    int index = i;
                    saveFiles.add(i, new File("SaveFiles/Saves/save" + i));
                    uiManager.addObject(new UIImageButton("Save", handler.getWidth() / 2, 150 + (index * 70), 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                        submenu = "Pause";
                        menuSwitch = true;
                        handler.getGame().save(saveFiles.get(index));
                    }));
                    uiManager.addObject(new UIImageButton("Load", handler.getWidth() / 2 + 150, 150 + (index * 70), 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                        Game game = handler.getGame().load(saveFiles.get(index));
                        game.setThread(new Thread());
                        game.start();
                        game.setState(States.GAME);
                        handler.setGame(game);
                    }));
                    uiManager.addObject(new UIImageButton("Delete", handler.getWidth() / 2 + 300, 150 + (index * 70), 128, 64, true, Assets.btn, (ClickListener & Serializable)() -> {
                        saveFiles.get(index).delete();
                    }));
                }
                break;
            case "LEVEL UP":
                //Create the buttons for the level up screen
                
                Stat[] stats = Stat.values(); //Store all of the possible stats to be leveled up in a local array
                //Iterate through the array to create a button for each stat
                for (int i = 0; i < stats.length; i++){
                    int index = i;
                    int xPos; //Special buffer used to determine the x-coordinate of each button
                    if (index < 4){
                        xPos = index - 2;
                        uiManager.addObject(new UIImageButton(stats[index].getValue(), (handler.getWidth() / 2) - (128 * xPos) - (32 * xPos), handler.getHeight() - 148, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                            PlayableActor play = actor; //Place the PlayableActor into a buffer so the global field can be set to null and the levelUp method can be called from the buffer
                            actor = null;
                            menuSwitch = true;
                            submenu = "Pause";
                            play.levelUp(stats[index]); //Make the button level up the given stat
                        }));
                    }
                    else{
                        xPos = index - 6;
                        uiManager.addObject(new UIImageButton(stats[index].getValue(), (handler.getWidth() / 2) - (128 * xPos) - (32 * xPos), handler.getHeight() - 74, 128, 64, true, Assets.btn,  (ClickListener & Serializable)() -> {
                            PlayableActor play = actor; //Place the PlayableActor into a buffer so the global field can be set to null and the levelUp method can be called from the buffer
                            actor = null;
                            menuSwitch = true;
                            submenu = "Pause";
                            play.levelUp(stats[index]); //Make the button level up the given stat
                        }));
                    }
                }
                break;
            default:
                //Shouldn't ever get here; if you do, FUCKING PANIC
                break;
        }
        
        menuSwitch = false;
    }
    
    public UIManager getUIManager() {
        return uiManager;
    }
    
    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    
    public String getSubmenu() {
        return submenu;
    }
    
    public void setSubmenu(String submenu) {
        this.submenu = submenu;
    }
    
    public PlayableActor getActor() {
        return actor;
    }
    
    public void setActor(PlayableActor actor) {
        this.actor = actor;
    }
    public boolean isMenuSwitch() {
        return menuSwitch;
    }

    public void setMenuSwitch(boolean menuSwitch) {
        this.menuSwitch = menuSwitch;
    }
}