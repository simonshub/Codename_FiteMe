/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states;

import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.states.cardcrafter.CardCrafterState;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.creaturecrafter.CreatureCrafterState;
import org.simon.src.game.states.menu.MenuState;
import org.simon.src.game.states.newgame.NewGameState;
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
            sbg.enterState(MenuState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD2) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(NewGameState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD3) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(CombatState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD4) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(CardCrafterState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD5) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(CreatureCrafterState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_NUMPAD6) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(CardGalleryState.ID);
            
            if (frame!=null) frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            return true;
        }
        
        return false;
        
    }
    
}
