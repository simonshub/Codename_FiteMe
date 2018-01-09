/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Graphics;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.game.data.gameplay.cards.Card;
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
    
    public void addSfx (Card parent, Creature src, List<Creature> targets) {
        Creature[] targets_arr = new Creature [targets.size()];
        targets.toArray(targets_arr);
        addSfx (parent, src, targets_arr);
    }
    
    public void addSfx (Card parent, Creature src, Creature... targets) {
        List<GuiElement> target_el_list = new ArrayList<> ();
        for (int i=0;i<targets.length;i++) {
            target_el_list.add(targets[i].getGuiElement());
        }
        GuiElement[] target_elements = new GuiElement [target_el_list.size()];
        target_el_list.toArray(target_elements);
        
        addSfx (parent, src.getGuiElement(), target_elements);
    }
    
    public void addSfx (Card parent, GuiElement src, GuiElement... target_elements) {
        for (GuiElement target_element : target_elements) {
            SpecialEffect sfx = new SpecialEffect (parent.getSfxCallstring(), src, target_element);
            sfx.setCallback(new SpecialEffectCallback (parent, src.getCreature(), target_element.getCreature()));
            instances.add(sfx);
        }
    }
    
    public void addSfx (StatusEffect parent, Creature src, Creature target) {
        SpecialEffect sfx = new SpecialEffect (parent.getSfxString(), src.getGuiElement(), target.getGuiElement());
        sfx.setCallback(new SpecialEffectCallback (parent, src, target));
        instances.add(sfx);
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
