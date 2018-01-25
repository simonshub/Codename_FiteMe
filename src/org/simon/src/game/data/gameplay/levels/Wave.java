/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.levels;

import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author emil.simon
 */
public class Wave {
    
    public static final int WAVE_MIN_SIZE = 1;
    public static final int WAVE_MAX_SIZE = 5;
    public static final int MAX_GENERATE_TRIES = 10;
    
    private final List<Creature> wave;
    
    
    
    Wave (Encounter parent, float target_difficulty) {
        float diff = 0f;
        wave = new ArrayList<> ();
        
        for (int i=0;i<MAX_GENERATE_TRIES;i++) {
            if (wave.size()>=WAVE_MAX_SIZE) break;
            
            Creature to_add = parent.getRandomCreature();
            
            if (diff + to_add.getDifficulty() < target_difficulty) {
                diff += to_add.getDifficulty();
                wave.add(to_add);
            } else {
                wave.add(to_add);
                break;
            }
        }
    }
    
    
    
    public List<Creature> getWaveCreatures () {
        return wave;
    }
    
    public float getTotalDifficulty () {
        float total = 0f;
        for (Creature c : wave) {
            if (c!=null) total += c.getDifficulty();
        }
        return total;
    }
    
}
