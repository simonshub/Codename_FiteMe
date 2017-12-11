/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.states.cardcrafter.CardCrafterState;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.creaturecrafter.CreatureCrafterState;
import org.simon.src.utils.Log;

/**
 *
 * @author XyRoN
 */
public class SharedState {
    
    private static int current_state_id;
    
    public static int getCurrentStateId () {
        return current_state_id;
    }
    
    public static void updateStateId (int new_id) {
        current_state_id = new_id;
    }
    
    public static boolean isCurrentState (Class<? extends BasicGameState> state) {
        int state_id = -1;
        try {
            state_id = state.getField("ID").getInt(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Log.err(ex);
        }
        return state_id == current_state_id;
    }
    
    public static boolean update (GameContainer gc, StateBasedGame sbg, JFrame frame) {
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD1) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            if (sbg.getCurrentStateID()==CombatState.ID)
                sbg.enterState(CardCrafterState.ID);
            else
                sbg.enterState(CombatState.ID);
            
            if (frame!=null) frame.setVisible(false);
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD2) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            if (sbg.getCurrentStateID()==CombatState.ID)
                sbg.enterState(CreatureCrafterState.ID);
            else
                sbg.enterState(CombatState.ID);
            
            if (frame!=null) frame.setVisible(false);
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD3) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            if (sbg.getCurrentStateID()==CombatState.ID)
                sbg.enterState(CreatureCrafterState.ID);
            else
                sbg.enterState(CombatState.ID);
            
            if (frame!=null) frame.setVisible(false);
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD4) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            if (sbg.getCurrentStateID()==CombatState.ID)
                sbg.enterState(CardGalleryState.ID);
            else
                sbg.enterState(CombatState.ID);
            
            if (frame!=null) frame.setVisible(false);
            return true;
        }
        
        return false;
        
    }
    
}
