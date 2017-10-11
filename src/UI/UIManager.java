/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Main.Handler;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class UIManager {
    private Handler handler; //The handler
    private ArrayList<UIObject> objects; //ArrayList of all UIObjects used in the current state
    
    public UIManager(Handler handler){
        this.handler = handler;
        objects = new ArrayList<UIObject>();
    }
    
    public void tick(){
        for (UIObject o : objects)
            o.tick();
    }
    
    public void render(Graphics g){
        
        for (UIObject o : objects)
            o.render(g);
    }
    
    public void onMouseMove(MouseEvent e){
        
        for (UIObject o : objects)
            o.onMouseMove(e);
    }
    
    public void onMouseRelease(MouseEvent e){
        for (UIObject o : objects)
            o.onMouseRelease(e);
    }
    
    /**
     * Adds an object to the object list
     * @param o The object to be added
     */
    public void addObject(UIObject o){
        objects.add(o);
    }
    
    /**
     * Removes an object from the object list
     * @param o The object to be removed
     */
    public void removeObject(UIObject o){
        objects.remove(o);
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ArrayList<UIObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<UIObject> objects) {
        this.objects = objects;
    }
}