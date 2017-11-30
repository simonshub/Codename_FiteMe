/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.utils.Log;

/**
 *
 * @author XyRoN
 */
public class GameplayManager {
    
    public enum Opponent {
        
        PLAYER ("Player"), ENEMY ("Enemy")
        
        ;
        
        public final String text;
        
        Opponent (String text) {
            this.text = text;
        }
        
        public Opponent opposite () {
            if (this.equals(PLAYER)) return ENEMY;
            return PLAYER;
        }
        
    }
    
    
    
    private Opponent current_opponent;
    
    private Creature current_casting_creature;
    private final List<Creature> enemy_board;
    private final List<Creature> ally_board;
    
    
    
    public GameplayManager () {
        current_opponent = Opponent.PLAYER;
        
        enemy_board = new ArrayList<> ();
        ally_board = new ArrayList<> ();
    }
    
    
    
    public void addEnemy (GuiElement element) {
        if (element.getCreatures().length == 1)
            enemy_board.add(current_casting_creature);
        else
            Log.err("Cannot add enemy to board since passed GUI element does not contain extactly one creature");
    }
    
    public void addAlly (GuiElement element) {
        if (element.getCreatures().length == 1)
            ally_board.add(current_casting_creature);
        else
            Log.err("Cannot add ally to board since passed GUI element does not contain extactly one creature");
    }
    
    public Opponent getCurrentOpponent () {
        return current_opponent;
    }
    
    public String getCurrentOpponentText () {
        return current_opponent.text;
    }
    
    public List<Creature> getEnemies () {
        return enemy_board;
    }
    
    public List<Creature> getAllies () {
        return ally_board;
    }
    
    public Creature getCurrentCastingCreature () {
        return current_casting_creature;
    }
    
    public void setCurrentCastingCreature (Creature current_casting_creature) {
        this.current_casting_creature = current_casting_creature;
    }
}
