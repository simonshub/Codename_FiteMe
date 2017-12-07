/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class GuiActionHandler {
    
    public static final float SCALE_SPEED_MODIFIER = 1000f; // determines the duration (in ms) needed
                                                            // to increase/decrease scale by it's speed,
                                                            // default 1 sec
    
    public static final String MULTICALL_DELIMITER = "&";
    
    
    
    private GuiActionHandler () { }
    
    private static void multicall (GuiElement source, String call_name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String[] names = call_name.split(MULTICALL_DELIMITER);
        for (String name : names) {
            Method callback = GuiActionHandler.class.getDeclaredMethod(name, GuiElement.class);
            callback.invoke(null, source);
        }
    }
    
    
    
    // insert gui action methods here ...
    
    public static void testclick (GuiElement source) {
        Log.log("Testing...");
    }
    
    public static void togglevisible (GuiElement source) {
        if (Settings.debug_gui) Log.log("togglevisible");
        source.setVisible(!source.isVisible());
    }
    
    public static void hide (GuiElement source) {
        if (Settings.debug_gui) Log.log("hide");
        source.setVisible(false);
    }
    
    public static void destroy (GuiElement source) {
        if (Settings.debug_gui) Log.log("destroy");
        Gui parent = source.getParent();
        parent.removeElement(source.getName());
    }
    
    public static void floatup (GuiElement source, float dt) {
        float real_base_y;
        if (source.hasProperty("real_base_y")) {
            real_base_y = (float)source.getProperty("real_base_y");
        } else {
            real_base_y = source.base_y;
            source.setProperty("real_base_y", real_base_y);
        }
        
        float speed = (float) source.getProperty("float_speed");
        float float_limit = (float)source.getProperty("float_limit");
        
        source.display_y = Math.max(real_base_y - float_limit, source.display_y - (speed * dt));
        
        if (source.hasProperty("floatup_callback")) {
            try {
                String call_name = (String) source.getProperty("floatup_callback");
                if (Settings.debug_gui) Log.log("floatup_callback: "+call_name);
                multicall(source, call_name);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
    }
    
    public static void floatbackdown (GuiElement source, float dt) {
        float real_base_y;
        if (source.hasProperty("real_base_y")) {
            real_base_y = (float)source.getProperty("real_base_y");
        } else {
            real_base_y = source.base_y;
            source.setProperty("real_base_y", real_base_y);
        }
        
        float speed = (float) source.getProperty("float_speed");
        
        if (source.base_y + (speed * dt) >= real_base_y) {
            source.base_y = real_base_y;
            source.display_y = source.base_y;
            return;
        }
        
        source.base_y = Math.min(real_base_y, source.base_y + (speed * dt));
        source.display_y += (speed * dt);
        
        if (source.hasProperty("floatbackdown_callback")) {
            try {
                String call_name = (String) source.getProperty("floatbackdown_callback");
                if (Settings.debug_gui) Log.log("floatbackdown_callback: "+call_name);
                multicall(source, call_name);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
    }
    
    public static void scaleup (GuiElement source, float dt) {
        if (CombatState.substate == CombatState.CombatSubState.PICK_TARGET) return;
        
        float speed = (float) source.getProperty("scale_speed");
        float limit = (float) source.getProperty("scale_limit");
        
        float y_per_x_ratio = source.base_height / source.base_width;
        
        // recalculate width and height ...
        source.scale = Math.min(source.scale + ((speed * dt) / SCALE_SPEED_MODIFIER), limit);
        float width_limit = source.base_width * limit;
        float height_limit = source.base_height * limit;
        float new_width = source.base_width * source.scale;
        float new_height = source.base_height * source.scale;
        
        // recalculate x and y ...
        float x_limit = source.base_x - (width_limit - source.base_width)/2f;
        float y_limit = source.base_y - (height_limit - source.base_height)/2f;
        float new_x = source.base_x - (new_width - source.base_width)/2f;
        float new_y = source.base_y - (new_height - source.base_height)/2f;
        
        // update values
        source.width = Math.min(width_limit, new_width);
        source.height = Math.min(height_limit, new_height);
        
        source.display_x = Math.max(x_limit, new_x);
        source.display_y = Math.max(y_limit, new_y);
    }
    
    public static void scalebackdown (GuiElement source, float dt) {
        float speed = (float) source.getProperty("scale_speed");
        float y_per_x_ratio = source.base_height / source.base_width;
        
        if (source.scale <= 1.0f) return;
        
        // recalculate width and height ...
        source.scale = Math.max(source.scale - ((speed * dt) / SCALE_SPEED_MODIFIER), 1.0f);
        float new_width = source.base_width * source.scale;
        float new_height = source.base_height * source.scale;
        
        if (new_width <= source.base_width || new_height <= source.base_height) return;
        
        // recalculate x and y ...
        float new_x = source.display_x + ((source.width - new_width)/2f);
        float new_y = source.display_y + ((source.height - new_height)/2f);
        
        // update values
        source.width = Math.max(source.base_width, new_width);
        source.height = Math.max(source.base_height, new_height);
        
        source.display_x = Math.min(source.base_x, new_x);
        source.display_y = Math.min(source.base_y, new_y);
    }
    
    public static void fadein (GuiElement source) {
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        if (Settings.debug_gui) Log.log("fadein");
        source.addOnUpdate("fadein_loop");
    }
    
    public static void fadein_loop (GuiElement source, float dt) {
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        float fade_speed = (float) source.getProperty("fade_speed") * ( dt / 1000f );
        
        Color col = source.getColor();
        float new_a = Math.min(col.a + fade_speed, (float) source.getProperty("start_a"));
        
        Color text_col = source.getTextColor();
        float new_text_a = Math.min(text_col.a + fade_speed, (float) source.getProperty("start_text_a"));
        
        source.setColor(col.r, col.g, col.b, new_a);
        source.setTextColor(text_col.r, text_col.g, text_col.b, new_text_a);
        
        if ( (new_a == (float) source.getProperty("start_a")) && (new_text_a == (float) source.getProperty("start_text_a")) ) {
            source.removeOnUpdate("fadein_loop");
            if (source.hasProperty("fadein_callback")) {
                try {
                    String call_name = (String) source.getProperty("fadein_callback");
                    if (Settings.debug_gui) Log.log("fadein_callback: "+call_name);
                    multicall(source, call_name);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    public static void fadeout (GuiElement source) {
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        if (Settings.debug_gui) Log.log("fadeout");
        source.addOnUpdate("fadeout_loop");
    }
    
    public static void fadeout_loop (GuiElement source, float dt) {
        if (source.hasOnUpdate("fadein_loop")) {
            source.removeOnUpdate("fadeout_loop");
            return;
        }
        
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        float fade_speed = (float) source.getProperty("fade_speed") * ( dt / 1000f );
        
        Color col = source.getColor();
        float new_a = Math.max(col.a - fade_speed, 0f);
        
        Color text_col = source.getTextColor();
        float new_text_a = Math.max(text_col.a - fade_speed, 0f);
        
        source.setColor(col.r, col.g, col.b, new_a);
        source.setTextColor(text_col.r, text_col.g, text_col.b, new_text_a);
        
        if ( (new_a == 0f) && (new_text_a == 0f) ) {
            source.removeOnUpdate("fadeout_loop");
            if (source.hasProperty("fadeout_callback")) {
                try {
                    String call_name = (String) source.getProperty("fadeout_callback");
                    if (Settings.debug_gui) Log.log("fadeout_callback: "+call_name);
                    multicall(source, call_name);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Log.err(ex);
                }
            }
        }
    }
    
    public static void printinfo (GuiElement source) {
        if (Settings.debug_gui) Log.log("printinfo");
        Log.log("Gui Element Info: '"+source.getName()+"'");
        Log.log("\tx: "+source.display_x+" ("+source.base_x+")");
        Log.log("\ty: "+source.display_y+" ("+source.base_y+")");
        Log.log("\twidth: "+source.width+" ("+source.base_width+")");
        Log.log("\theight: "+source.height+" ("+source.base_height+")");
        Log.log("\tlayer: "+source.layer);
    }
    
    public static void printcardinfo (GuiElement source) {
        if (Settings.debug_gui) Log.log("printcardinfo");
        if (source.getCard() == null) return;
        
        Card card = source.getCard();
        Log.log("Card Info: "+card.getName()+" ("+card.getId()+")\n"+card.toString());
    }
    
    public static void printcreatureinfo (GuiElement source) {
        if (Settings.debug_gui) Log.log("printcreatureinfo");
        if (source.getCreatures() == null) return;
        
        Creature[] creatures = source.getCreatures();
        String creature_string = "";
        for (int i=0;i<creatures.length;i++) {
            creature_string += creatures[i].getName()+" ("+creatures[i].getCurrentHealth()+"/"+creatures[i].getMaxHealth()+")" + creatures[i].toString();
        }
        Log.log("Creature Info:\n"+creature_string);
    }
    
    public static void clear_onclick (GuiElement source) {
        if (Settings.debug_gui) Log.log("clear_onclick");
        source.setOnClick("");
    }
    
    public static void set_onclick (GuiElement source) {
        if (Settings.debug_gui) Log.log("set_onclick");
        if (source.hasProperty("set_onclick"))
            source.setOnClick((String) source.getProperty("set_onclick"));
    }
    
    public static void set_onupdate (GuiElement source) {
        if (Settings.debug_gui) Log.log("set_onupdate");
        if (source.hasProperty("set_onupdate"))
            source.setOnClick((String) source.getProperty("set_onupdate"));
    }
    
    public static void set_text (GuiElement source) {
        if (Settings.debug_gui) Log.log("set_text");
        if (source.hasProperty("set_text"))
            source.setText((String) source.getProperty("set_text"));
    }
    
    public static void remove_fadeout_callback (GuiElement source) {
        if (Settings.debug_gui) Log.log("remove_fadeout_callback");
        source.removeProperty("fadeout_callback");
    }
    
    
    
    public static void selecttarget (GuiElement source) {
        if (CombatState.substate == CombatState.CombatSubState.PICK_TARGET) {
            Gui parent = source.getParent();

            GuiElement el = parent.getElement("played_card");
            el.setVisible(false);
            Card card = el.getCard();
            Creature[] targets = source.getCreatures();

            card.play(CombatState.sfx, CombatState.getCurrentCastingCreature(), targets);
            CombatState.substate = CombatState.CombatSubState.PICK_CARD;

            GuiElement tut = parent.getElement("tutorial_label");
            tut.setOnUpdate("fadeout_loop");
            tut.setProperty("fadeout_callback", "set_text");
            tut.setProperty("set_text", TutorialStringLibrary.PICK_A_CARD);
        } else if (CombatState.substate == CombatState.CombatSubState.PICK_CARD && source.getName().contains("_ally_")) {
            CombatState.setCurrentTurnCreature(source);
        }
    }
    
    public static void selectcard (GuiElement source) {
        if (CombatState.substate != CombatState.CombatSubState.PICK_CARD) return;
        if (CombatState.gameplay.getCurrentOpponent()==GameplayManager.Opponent.PLAYER && !CombatState.getCurrentCastingCreature().canSpendPoints(source.getCard())) return;
        
        Gui parent = source.getParent();
        
        GuiElement el = parent.getElement("played_card");
        el.setVisible(true);
        el.setCard(source.getCard());
        CombatState.substate = CombatState.CombatSubState.PICK_TARGET;
        
        GuiElement tut = parent.getElement("tutorial_label");
        tut.setOnUpdate("fadein_loop");
        tut.setProperty("fadein_callback", "set_onclick");
        tut.setProperty("set_onclick", "fadeout");
        tut.setText(TutorialStringLibrary.PICK_A_TARGET);
        if (Settings.debug_gui) Log.log("fadein");
    }
    
    public static void deselectcard (GuiElement source) {
        if (CombatState.substate == CombatState.CombatSubState.PICK_CARD) return;
        
        Gui parent = source.getParent();
        
        GuiElement el = parent.getElement("played_card");
        el.setVisible(false);
        CombatState.substate = CombatState.CombatSubState.PICK_CARD;
        
        GuiElement tut = parent.getElement("tutorial_label");
        tut.setOnUpdate("fadeout_loop");
    }
    
    public static void endturn (GuiElement source) {
        if (CombatState.substate != CombatState.CombatSubState.PICK_CARD)
            return;
        
        CombatState.endTurn();
    }
    
    public static void closeup (GuiElement source) {
        CardGalleryState.switchToCloseup(source.getCard());
    }
    
}
