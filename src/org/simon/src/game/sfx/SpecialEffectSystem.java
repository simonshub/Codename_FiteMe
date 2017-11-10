/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class SpecialEffectSystem {
    
    private List<SpecialEffect> instances;
    
    
    
    public SpecialEffectSystem () {
        instances = new ArrayList<> ();
    }
    
    
    
    public void playSfx () {
        for (int i=0;i<instances.size();i++) {
            instances.get(i).start();
        }
    }
    
    public void reset () {
        instances = new ArrayList<> ();
    }
    
    public void addSfx (String sfx_callstring, SpecialEffectCallback callback, Creature src, Creature... targets) {
        List<GuiElement> target_el_list = new ArrayList<> ();
        for (int i=0;i<targets.length;i++) {
            target_el_list.add(targets[i].getGuiElement());
        }
        GuiElement[] target_elements = new GuiElement [target_el_list.size()];
        target_el_list.toArray(target_elements);
        
        addSfx (sfx_callstring, callback, src.getGuiElement(), target_elements);
    }
    
    public void addSfx (String sfx_callstring, SpecialEffectCallback callback, GuiElement src, GuiElement... target_elements) {
        for (GuiElement target_element : target_elements) {
            SpecialEffect sfx = new SpecialEffect (sfx_callstring, src, target_element);
            
            if (callback!=null) sfx.setCallback(callback);
            instances.add(sfx);
        }
    }
    
    
    
    public void render (Graphics g) {
        for (int i=0;i<instances.size();i++) {
            instances.get(i).render(g);
        }
    }
    
    public void update (int dt) {
        for (int i=0;i<instances.size();i++) {
            SpecialEffect sfx = instances.get(i);
            
            if (!sfx.update(dt)) {
                if (Settings.debug_sfx) Log.log("Special effect at index "+String.valueOf(i+1)+" has finished");
                if (Settings.debug_cards) {
                    Log.log("Invoking special effect callback '"+sfx.getCallback().toString()+"' ...");
                }
                
                instances.remove(i);
                if (Settings.debug_sfx) Log.log("Special effect at index "+String.valueOf(i+1)+" was removed");
                i--;
            }
        }
    }
    
    
}
