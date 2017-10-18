/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enums;

/**
 * An enum for holding character types, used when distinguishing exactly which character can do which
 * things (i.e. casting spells, using skills, etc).
 * @author Soup
 */
public enum Characters {
    SARIEL("Sariel"),
    ZANNA("Zanna"),
    RYNN("Rynn"),
    THERON("Theron"),
    RIBEL("Ribel");
    
    private String value; //The value of each enum; should be a String containing the base name of the character
    
    private Characters(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
