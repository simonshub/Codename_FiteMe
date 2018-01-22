/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author emil.simon
 */
public class BoardState implements Serializable {
    protected int wave;
    protected int total_difficulty_so_far;
    protected String level_type;
    protected List<CreatureState> enemy_board;
    
    protected void apply () {
        GameplayManager.setWave(wave);
        GameplayManager.setTotalDifficulty(total_difficulty_so_far);
        GameplayManager.setLevelType(level_type);
        
        GameplayManager.clearEnemies();
        for (int i=0;i<enemy_board.size();i++) {
            Creature creature = enemy_board.get(i).asCreature();
            GameplayManager.addEnemy(creature);
        }
    }
    
    protected void applyStatusEffects () {
        for (int i=0;i<enemy_board.size();i++) {
            enemy_board.get(i).applyStatusEffects();
        }
    }
}
