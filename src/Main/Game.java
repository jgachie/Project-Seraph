/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Main;

import Display.Display;
import Entities.Creatures.Actors.Player;
import Graphics.Assets;
import Graphics.GameCamera;
import Graphics.ImageLoader;
import Graphics.SpriteSheet;
import Input.KeyManager;
import Input.MouseManager;
import States.*;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

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
    
    //Initialization
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
        State.setState(menuState); //Set current state to game state
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
    
    public synchronized void start(){
        //If the game is already running, do nothing and leave
        if (running)
            return;
        
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
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

* Three different stances: Offensive, defensive, and neutral. Offensive provides a boost to strength,
dexterity, and wisdom, but reduces defense, evasion, and intelligence. Defensive does the exact opposite,
while neutral maintains a balance between the stats. Stances can only be changed before battle (maybe);
some attacks, spells, etc. require you to be in a certain stance in order to use them, while others 
might put you in a different stance after using them.
*/