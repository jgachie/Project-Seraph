/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Enums;

/**
 *
 * @author Soup
 */
public enum Stat {
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    WISDOM("Wisdom"),
    INTELLIGENCE("Intelligence"),
    LUCK("Luck"),
    DEFENSE("Defense"),
    AGILITY("Agility"),
    SKILL("Skill");
    
    private final String value; //The name of the stat
    
    private Stat(String value){
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
