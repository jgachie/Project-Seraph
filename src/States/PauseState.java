/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package States;

import Main.Handler;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 *
 * @author Soup
 */
public class PauseState extends State{

    public PauseState(Handler handler) {
        super(handler);
    }

    @Override
    public void tick() {
        //When the enter key is pressed, switch back to the game
        if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER))
            handler.getGame().setState("Game");
        
    }

    @Override
    public void render(Graphics g) {
        
    }
    
}
