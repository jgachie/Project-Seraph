/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Main.Handler;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Soup
 */
public class UIManager implements Serializable{
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
        for (int i = 0; i < objects.size(); i++)
            objects.get(i).render(g);
    }
    
    public void onMouseMove(MouseEvent e){
        for (int i = 0; i < objects.size(); i++)
            objects.get(i).onMouseMove(e);
    }
    
    public void onMouseRelease(MouseEvent e){
        for (int i = 0; i < objects.size(); i++)
            objects.get(i).onMouseRelease(e);
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
    
    /**
     * Removes the object at the given index from the object list
     * @param index The index of the object to be removed
     */
    public void removeObject(int index){
        objects.remove(index);
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