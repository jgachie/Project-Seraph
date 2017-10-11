/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package Input;

import UI.UIManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Soup
 */
public class MouseManager implements MouseListener, MouseMotionListener{
    private boolean leftPressed, rightPressed, middlePressed; //Whether mouse buttons are being pressed
    private int mouseX, mouseY; //The coordinates of the mouse cursor
    private UIManager manager; //The UI Manager
    
    public MouseManager(){
        
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        //If left/right mouse button is being pressed, set corresponding boolean to true
        if (e.getButton() == MouseEvent.BUTTON1)
            leftPressed = true;
        else if (e.getButton() == MouseEvent.BUTTON3)
            rightPressed = true;
        else if (e.getButton() == MouseEvent.BUTTON2)
            middlePressed = true;
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        //If left/right mouse button is being released, set corresponding boolean to false
        if (e.getButton() == MouseEvent.BUTTON1)
            leftPressed = false;
        else if (e.getButton() == MouseEvent.BUTTON3)
            rightPressed = false;
        else if (e.getButton() == MouseEvent.BUTTON2)
            middlePressed = false;
        
        //If there is a ui manager, pass the event on through it so that any applicable UIObjects are triggered
        if (manager != null)
            manager.onMouseRelease(e);
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        //Set current coordinates of mouse cursor
        mouseX = e.getX();
        mouseY = e.getY();
        
        //If there is a ui manager, pass the event on through it so that any applicable UIObjects are triggered
        if (manager != null)
            manager.onMouseMove(e);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    
    public boolean isLeftPressed(){
        return leftPressed;
    }
    
    public boolean isRightPressed(){
        return rightPressed;
    }
    
    public boolean isMiddlePressed(){
        return middlePressed;
    }
    
    public int getMouseX(){
        return mouseX;
    }
    
    public int getMouseY(){
        return mouseY;
    }
    
    public void setUIManager(UIManager manager){
        this.manager = manager;
    }
}