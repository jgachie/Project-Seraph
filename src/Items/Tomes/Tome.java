/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Items.Tomes;

import Entities.Creatures.Actors.PlayableActors.PlayableActor;
import Entities.Specials.Skills.Basic.*;
import Entities.Specials.Skills.Skill;
import Items.Equipment.Equipment;
import Enums.Characters;
import Enums.TomeType;
import Graphics.Assets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Soup
 */
public class Tome extends Equipment{
    //Hard-code all different types of tomes here
    public static Tome[] tomes = new Tome[256];
    public static Tome basicTome = new Tome(Assets.stone, "Basic Tome", "T001", new int[]{0, 0, 3, 3, 0, 0, 0, 0},
            TomeType.BASIC, new ArrayList<Skill>(){{
                add(new TaintedEdge()); add(new Accelerate()); add(new DragonSkin()); add(new ShadowStrike()); add(new Nightmare());
            }});
    
    protected final TomeType TYPE; //The tome's type
    protected final ArrayList<Skill> skills; //The skills the tome contains
    
    protected Tome(BufferedImage texture, String name, String ID, int[] statReqs, TomeType type,
            ArrayList<Skill> skills, Characters... users) {
        super(texture, name, ID, statReqs, new ArrayList<Characters>(Arrays.asList(users)));
        this.TYPE = type;
        this.skills = skills;
    }
    
    @Override
    public void equip(PlayableActor actor){
        if (checkChar(actor.getCharacter()) && checkStats(actor.getStats()))
            actor.setTome(this);
    }
    
    public TomeType getType() {
        return TYPE;
    }
    
    public ArrayList<Skill> getSkills() {
        return skills;
    }
}
