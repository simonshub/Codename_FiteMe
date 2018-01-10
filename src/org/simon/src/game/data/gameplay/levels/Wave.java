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
    
    public static final int WAVE_MIN_SIZE = 3;
    public static final int WAVE_MAX_SIZE = 5;
    
    private final List<Creature> wave;
    
    public Wave (Encounter parent) {
        int n_of_creatures = SlickUtils.rand(WAVE_MIN_SIZE,WAVE_MAX_SIZE);
        wave = new ArrayList<> ();
        
        for (int i=0;i<n_of_creatures;i++)
            wave.add(parent.getRandomCreature());
    }
    
    public List<Creature> getWaveCreatures () {
        return wave;
    }
    
    public float getTotalDifficulty () {
        float total = 0f;
        for (Creature c : wave) {
            total += c.getDifficulty();
        }
        return total;
    }
    
}
