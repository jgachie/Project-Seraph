/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Display.Display;

/**
 *
 * @author Soup
 */
public class Launcher {
    
    public static void main(String[] args) {
        Game game = new Game("Project Sariel", 1024, 768); //Initialize game with title, width, and height
        game.start(); //Start game
    }
}