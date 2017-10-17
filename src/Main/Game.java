/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Display.Display;
import Entities.Creatures.Actors.PlayableActors.Player;
import Graphics.Assets;
import Graphics.GameCamera;
import Input.KeyManager;
import Input.MouseManager;
import States.*;
import UI.UITextBox;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

/**
 *
 * @author Soup
 */
public class Game implements Runnable{
    private Display display; //Game window
    public String title; //Window title;
    private int width, height; //Window width/height
    
    private boolean running = false; //Whether the game is currently running or not
    private Thread thread; //Main thread
    
    //Graphics shit
    private BufferStrategy bs;
    private Graphics g;
    
    //States
    private State gameState;
    private State menuState;
    
    //Input
    private KeyManager keyManager;
    private MouseManager mouseManager;
    
    //Camera
    private GameCamera gameCamera;
    
    //Handler
    private Handler handler;
    
    public Game(String title, int width, int height){
        this.width = width;
        this.height = height;
        this.title = title;
        
        //Initialize input managers
        keyManager = new KeyManager();
        mouseManager = new MouseManager();
    }
    
    /**
     * Game initialization
     */
    private void init(){
        display = new Display(title, width, height);
        
        //Add input managers to window (add mouse manager to canvas too to account for focusing)
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        
        //Asset initialization
        Assets.init();
        
        //Utility initialization
        handler = new Handler(this);
        gameCamera = new GameCamera(handler, 0, 0); //Set initial offsets to 0 for base position
        
        //Playable character initialization
        new Player(handler, 0, 0, "Sariel").save();
        
        //State initialization
        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        State combatState = new CombatState(handler);
        State.setState(combatState); //Set current state to game state
        
        //Stream initializatoin
        System.setOut(UITextBox.getStream()); //Redirect the system's PrintStream to the text box
    }
    
    private void tick(){
        keyManager.tick();
        
        //Make sure current state isn't null before ticking
        if (State.getState() != null)
            State.getState().tick();
    }
    
    private void render(){
        bs = display.getCanvas().getBufferStrategy(); //Get canvas's buffer strategy
        
        //Runs if canvas doesn't have buffer strategy (when game first starts up)
        if (bs == null){
            display.getCanvas().createBufferStrategy(3); //Create buffer strategy (don't need more than 3)
            return;
        }
        
        g = bs.getDrawGraphics(); //Get bs's graphics object
        
        g.clearRect(0, 0, width, height); //Clear screen before drawing
        
        //!--Draw here--!
        
        //Make sure current state isn't null before rendering
        if (State.getState() != null)
            State.getState().render(g);
        //!--End drawing--!
        
        bs.show(); //Display buffered images to the screen
        g.dispose(); //Dispose of graphics object
    }
    
    @Override
    public void run(){
        init();
        
        int fps = 60; //Set fps to 60
        double timePerTick = 1000000000 / fps; //Set max time per tick by dividing 1 billion nanoseconds (1 second) by 60 fps
        double delta = 0; //Time difference betweeen ticks
        long now; //Computer's current time in nanoseconds
        long lastTime = System.nanoTime(); //Computer's previous time in nanoseconds
        
        //Game loop
        while (running){
            now = System.nanoTime(); //Set current computer time
            delta += (now - lastTime) / timePerTick; //Get time difference between ticks and divide by time per tick
            lastTime = now; //"Now" is now "last time"
            
            //If the time differece is over 1, tick and render
            if (delta >= 1){
                tick();
                render();
                delta--; //Tick and render has occurred; delta can be set back by one
            }
        }
        
        stop();
    }
    
    public State getState(String state){
        switch (state.toUpperCase()){
            case "GAME":
                return gameState;
            case "MENU":
                return menuState;
            default:
                return gameState;
        }
    }
    
    public KeyManager getKeyManager(){
        return keyManager;
    }
    
    public MouseManager getMouseManager(){
        return mouseManager;
    }
    
    public GameCamera getGameCamera(){
        return gameCamera;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    /**
     * Starts the game
     */
    public synchronized void start(){
        //If the game is already running, do nothing and leave
        if (running)
            return;
        
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    /**
     * Ends the game
     */
    public synchronized void stop(){
        //If the game is already stopped, do nothing and leave
        if (!running)
            return;
        
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

/*
Notes

                                        ***TO BE DONE***

* Three different stances: Offensive, defensive, and neutral. Offensive provides a boost to strength,
dexterity, and wisdom, but reduces defense, evasion, and intelligence. Defensive does the exact opposite,
while neutral maintains a balance between the stats. Stances can only be changed before battle (maybe);
some attacks, spells, etc. require you to be in a certain stance in order to use them, while others 
might put you in a different stance after using them.

* Sometimes, before a battle, a scene may play out in which the Player is presented with two or more
dialogue options. Choosing an option will provide the Player with a temporary boost to one of their
stats for the duration of the battle; the stat to be boosted will depend on the dialogue option chosen.
However, these dialogue options may have an impact on the story later in the game, and thus should not
be taken lightly.

* Throughout the story, the Player will be presented with options on the actions he/she may take, which
eventually have an impact on the rest of the story. However, in some cases, having certain stat levels
(particularly the Chaos stat; maybe the other stats as well) will restrict the options available to 
the Player, and sometimes even force them to choose a specific option. For example, at one point the
Player may be presented with the opportunity to spare an enemy or kill it mercilessly, but if his/her
Chaos stat is too high, the Player will be forced to slay the enemy.

* Add a Charm equipment type. Charms can be equipped to different characters in their charm slots, and,
in additon to boosting (or in some cases lowering) character stats, they often have unique effects that
range from helping the player in battle to opening new avenues of character interactions to even affecting
the story.

* Yffia (Sariel's mother) kept a diary after her son was taken from her. Gathering the scattered pages
will provide the player with backstory on the events that took place 700 years ago, including the Ash
Rebellion, the Six Years of Shadow, Yffia and the Willful's government reconstruction efforts, and her
search for her lost son. Collecting all of the pages yields the player with a new charm: Yffia's Diary.
When equipped, it reduces the Player's Chaos stat by 8, and has the hidden effect of preventing high
Chaos levels from making decisions for the Player.


                                        ***TO BE DECIDED***

* Make Rynn a man.

* Add an option at the beginning of the game for the player to choose the player character's gender.
This could lead into romantic subplots in the game which would deepen significance of player-character
interactions.
*/