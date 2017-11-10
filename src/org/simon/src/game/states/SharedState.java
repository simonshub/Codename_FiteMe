/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states;

import javax.swing.JFrame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.states.cardcrafter.CardCrafterState;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.creaturecrafter.CreatureCrafterState;

/**
 *
 * @author XyRoN
 */
public class SharedState {
    
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
