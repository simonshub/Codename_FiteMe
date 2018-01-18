/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.levels;

import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.utils.Log;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
class Encounter {
    
    public static final int MAX_WAVE_GENERATION_ATTEMPTS = 10;
    public static final float MAX_TARGET_DIFFICULTY = 1.2f; // max 120% of intended difficulty rating
    
    private final List<Creature> creature_list;
    
    public Encounter (String creature_list_string) {
        this.creature_list = new ArrayList<> ();
        
        String[] creature_names = creature_list_string.split(LevelType.ENCOUNTER_LISTING_DELIMITER);
        creature_names = SlickUtils.Strings.trimAll(creature_names);
        for (String creature_name : creature_names) {
            if (CreatureLibrary.hasCreature(creature_name.trim().toLowerCase())) {
                creature_list.add(CreatureLibrary.getCreatureRaw(creature_name));
            } else {
                Log.err("Tried to add creature '"+creature_name+"' to encounter, but no such creature was loaded...");
            }
        }
    }
    
    public List<Creature> getCreatureList () {
        return creature_list;
    }
    
    public Creature getRandomCreature () {
        return new Creature ((Creature) SlickUtils.randListObject(creature_list));
    }
    
}
