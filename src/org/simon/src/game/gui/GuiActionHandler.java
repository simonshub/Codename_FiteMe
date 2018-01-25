/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.TargetEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;
import org.simon.src.game.data.save.SavedStateFactory;
import org.simon.src.game.states.cardgallery.CardGalleryState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.newgame.NewGameState;
import org.simon.src.main.Main;
import org.simon.src.utils.CycleList;
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
    
    private static void multicall (final GuiElement source, String call_name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        String[] names = call_name.split(MULTICALL_DELIMITER);
        for (String name : names) {
            Method callback = GuiActionHandler.class.getDeclaredMethod(name, GuiElement.class);
            callback.invoke(null, source);
        }
    }
    
    
    
    // insert gui action methods here ...
    
    public static void testclick (final GuiElement source) {
        Log.log("Testing...");
    }
    
    public static void togglevisible (final GuiElement source) {
        if (Settings.debug_gui) Log.log("togglevisible ["+source.getName()+"]");
        source.setVisible(!source.isVisible());
    }
    
    public static void hide (final GuiElement source) {
        if (Settings.debug_gui) Log.log("hide ["+source.getName()+"]");
        source.setVisible(false);
    }
    
    public static void destroy (final GuiElement source) {
        if (Settings.debug_gui) Log.log("destroy ["+source.getName()+"]");
        GuiController parent = source.getParent();
        parent.removeElement(source.getName());
    }
    
    public static void floatup (final GuiElement source, float dt) {
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
    
    public static void floatbackdown (final GuiElement source, float dt) {
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
    
    public static void scaleup (final GuiElement source, float dt) {
        if (CombatState.substate == CombatState.CombatSubState.PICK_TARGET) return;
        
        float speed = (float) source.getProperty("scale_speed");
        float limit = (float) source.getProperty("scale_limit");
        
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
    
    public static void scalebackdown (final GuiElement source, float dt) {
        float speed = (float) source.getProperty("scale_speed");
        
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
    
    public static void fadein (final GuiElement source) {
        if (Settings.debug_gui) Log.log("fadein ["+source.getName()+"]");
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        if (Settings.debug_gui) Log.log("fadein");
        source.addOnUpdate("fadein_loop");
    }
    
    public static void fadein_loop (final GuiElement source, float dt) {
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
    
    public static void fadeout (final GuiElement source) {
        if (Settings.debug_gui) Log.log("fadeout ["+source.getName()+"]");
        if (!source.hasProperty("start_a") || !source.hasProperty("start_text_a")) {
            source.setProperty("start_a", source.getColor().a);
            source.setProperty("start_text_a", source.getTextColor().a);
        }
        
        if (Settings.debug_gui) Log.log("fadeout");
        source.addOnUpdate("fadeout_loop");
    }
    
    public static void fadeout_loop (final GuiElement source, float dt) {
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
    
    public static void printinfo (final GuiElement source) {
        if (Settings.debug_gui) Log.log("printinfo ["+source.getName()+"]");
        Log.log("Gui Element Info: '"+source.getName()+"'");
        Log.log("\tx: "+source.display_x+" ("+source.base_x+")");
        Log.log("\ty: "+source.display_y+" ("+source.base_y+")");
        Log.log("\twidth: "+source.width+" ("+source.base_width+")");
        Log.log("\theight: "+source.height+" ("+source.base_height+")");
        Log.log("\tlayer: "+source.layer);
    }
    
    public static void printcardinfo (final GuiElement source) {
        if (Settings.debug_gui) Log.log("printcardinfo ["+source.getName()+"]");
        if (source.getCard() == null) return;
        
        Card card = source.getCard();
        Log.log("Card Info: "+card.getName()+" ("+card.getId()+")\n"+card.toString());
    }
    
    public static void printcreatureinfo (final GuiElement source) {
        if (Settings.debug_gui) Log.log("printcreatureinfo ["+source.getName()+"]");
        if (source.getCreature() == null) return;
        
        Creature creature = source.getCreature();
        String creature_string = "";
        creature_string += creature.getName()+" ("+creature.getCurrentHealth()+"/"+creature.getMaxHealth()+")" + creature.toString();
        Log.log("Creature Info:\n"+creature_string);
    }
    
    public static void clear_onclick (final GuiElement source) {
        if (Settings.debug_gui) Log.log("clear_onclick ["+source.getName()+"]");
        source.setOnClick("");
    }
    
    public static void set_onclick (final GuiElement source) {
        if (Settings.debug_gui) Log.log("set_onclick ["+source.getName()+"]");
        if (source.hasProperty("set_onclick"))
            source.setOnClick((String) source.getProperty("set_onclick"));
    }
    
    public static void set_onupdate (final GuiElement source) {
        if (Settings.debug_gui) Log.log("set_onupdate ["+source.getName()+"]");
        if (source.hasProperty("set_onupdate"))
            source.setOnClick((String) source.getProperty("set_onupdate"));
    }
    
    public static void set_text (final GuiElement source) {
        if (Settings.debug_gui) Log.log("set_text ["+source.getName()+"]");
        if (source.hasProperty("set_text"))
            source.setText((String) source.getProperty("set_text"));
    }
    
    public static void remove_fadeout_callback (final GuiElement source) {
        if (Settings.debug_gui) Log.log("remove_fadeout_callback ["+source.getName()+"]");
        source.removeProperty("fadeout_callback");
    }
    
    public static void enter_state (final GuiElement source) {
        if (Settings.debug_gui) Log.log("enter_state ["+source.getName()+"]");
        if (source.hasProperty("enter_state")) {
            int new_state_id = (Integer) source.getProperty("enter_state");
            Main.instance.enterState(new_state_id);
        }
    }
    
    public static void exit (final GuiElement source) {
        Log.log("Exiting game...");
        System.exit(1);
    }
    
    public static void hover_img (final GuiElement source) {
        if (Settings.debug_gui) Log.log("hover_img ["+source.getName()+"]");
        if (source.hasProperty("hover_img")) {
            source.setProperty("unhover_img", source.getImageName());
            source.setImage((String) source.getProperty("hover_img"));
        }
    }
    
    public static void unhover_img (final GuiElement source) {
        if (Settings.debug_gui) Log.log("unhover_img ["+source.getName()+"]");
        if (source.hasProperty("unhover_img")) {
            source.setImage((String) source.getProperty("unhover_img"));
        }
    }
    
    
    
    public static void selecttarget (final GuiElement source) {
        if (Settings.debug_gui) Log.log("selecttarget ["+source.getName()+"]");
        
        // BUG: when a non-appropriate target is chosen (eg. GuiElement with no creature,
        // a dead creature, or friendly creature instead of enemy), the combat state gets
        // stuck in it's 'pick a target' state, even though it hides the played card 
        // gui element...
        
        if (CombatState.substate == CombatState.CombatSubState.PICK_TARGET) {
            GuiController parent = source.getParent();
            
            GuiElement played_card = parent.getElement("played_card");
            
            String source_card_el_name = (String) played_card.getProperty("source_card");
            GuiElement source_card_el = parent.getElement(source_card_el_name);
            
            Card card = played_card.getCard();
            Creature primary_target = source.getCreature();
            if (primary_target==null || primary_target.isDead()) return;
            Creature caster = GameplayManager.getCurrentCastingCreature();
            List<Creature> targets = TargetEnum.getTargetList (card.getTargetMode(), caster, primary_target, GameplayManager.getEnemies(), GameplayManager.getAllies());
            if (targets.isEmpty()) return;
            
            played_card.setVisible(false);
            source_card_el.setCardPlayed(true);
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
    
    public static void selectcard (final GuiElement source) {
        if (Settings.debug_gui) Log.log("selectcard ["+source.getName()+"]");
        if (CombatState.substate != CombatState.CombatSubState.PICK_CARD) return;
        if (GameplayManager.getCurrentOpponent()==GameplayManager.Opponent.OPPONENT && !CombatState.getCurrentCastingCreature().canSpendPoints(source.getCard())) return;
        
        GuiController parent = source.getParent();
        
        GuiElement played_card = parent.getElement("played_card");
        played_card.setVisible(true);
        played_card.setCard(source.getCard());
        played_card.setProperty("source_card", source.getName());
        CombatState.substate = CombatState.CombatSubState.PICK_TARGET;
        
        GuiElement tut = parent.getElement("tutorial_label");
        tut.setOnUpdate("fadein_loop");
        tut.setProperty("fadein_callback", "set_onclick");
        tut.setProperty("set_onclick", "fadeout");
        tut.setText(TutorialStringLibrary.PICK_A_TARGET);
        if (Settings.debug_gui) Log.log("fadein");
    }
    
    public static void deselectcard (final GuiElement source) {
        if (Settings.debug_gui) Log.log("deselectcard ["+source.getName()+"]");
        if (CombatState.substate == CombatState.CombatSubState.PICK_CARD) return;
        
        GuiController parent = source.getParent();
        
        GuiElement played_card = parent.getElement("played_card");
        played_card.setVisible(false);
        played_card.setProperty("source_card", "");
        CombatState.substate = CombatState.CombatSubState.PICK_CARD;
        
        GuiElement tut = parent.getElement("tutorial_label");
        tut.setOnUpdate("fadeout_loop");
    }
    
    public static void endturn (final GuiElement source) {
        if (Settings.debug_gui) Log.log("endturn ["+source.getName()+"]");
        if (CombatState.substate != CombatState.CombatSubState.PICK_CARD)
            return;
        
        CombatState.endTurn();
    }
    
    public static void closeup (final GuiElement source) {
        if (Settings.debug_gui) Log.log("closeup ["+source.getName()+"]");
        CardGalleryState.switchToCloseup(source.getCard());
    }
    
    public static void start_new_game (final GuiElement source) {
        if (Settings.debug_gui) Log.log("start_new_game ["+source.getName()+"]");
        GuiElement overlay = source.getParent().getElement("overlay");
        overlay.setVisible(true);
        overlay.instantCall("fadein");
        
        GameplayManager.init();
        GameplayManager.setIsNewGame(true);
        CycleList<PlayerCharacterClass> all_classes = GameplayManager.getAllPlayerCharacterClassesCycleList();
        Player.clearParty();
        
        for (int i = 0;i < 4;i++) {
            GuiElement char_picker = source.getParent().getElement("char_picker_portrait_"+i);
            int selected_index = (int) char_picker.getProperty("current_class_selection_index");
            PlayerCharacterClass char_class = all_classes.get(selected_index);
            Player.addCharacterToParty(char_class);
        }
    }
    
    public static void continue_game (final GuiElement source) {
        if (Settings.debug_gui) Log.log("continue_game ["+source.getName()+"]");
        GuiElement overlay = source.getParent().getElement("overlay");
        overlay.setVisible(true);
        overlay.instantCall("fadein");
        
        GameplayManager.init();
        GameplayManager.setIsNewGame(false);
        SavedStateFactory.load();
    }
    
    public static void unpause_combatstate (final GuiElement source) {
        if (Settings.debug_gui) Log.log("unpause_combatstate ["+source.getName()+"]");
        CombatState.paused = false;
    }
    
    public static void next_class (final GuiElement source) {
        if (Settings.debug_gui) Log.log("next_class ["+source.getName()+"]");
        int index = (int) source.getProperty("char_picker_slot_index");
        
        GuiElement portrait = source.getParent().getElement("char_picker_portrait_" + index);
        GuiElement label = source.getParent().getElement("char_picker_label_" + index);
        
        int current_selection = (int) portrait.getProperty("current_class_selection_index");
        int new_selection = current_selection + 1;
        
        PlayerCharacterClass selection = NewGameState.all_classes.get(new_selection);
        portrait.setProperty("current_class_selection_index", new_selection);
        portrait.setImage(selection.getPortrait());
        label.setText(selection.getName());
    }
    
    public static void prev_class (final GuiElement source) {
        if (Settings.debug_gui) Log.log("next_class ["+source.getName()+"]");
        int index = (int) source.getProperty("char_picker_slot_index");
        
        GuiElement portrait = source.getParent().getElement("char_picker_portrait_" + index);
        GuiElement label = source.getParent().getElement("char_picker_label_" + index);
        
        int current_selection = (int) portrait.getProperty("current_class_selection_index");
        int new_selection = current_selection - 1;
        
        PlayerCharacterClass selection = NewGameState.all_classes.get(new_selection);
        portrait.setProperty("current_class_selection_index", new_selection);
        portrait.setImage(selection.getPortrait());
        label.setText(selection.getName());
    }
    
    public static void clear_creature (final GuiElement source) {
        if (Settings.debug_gui) Log.log("clear_creature ["+source.getName()+"]");
        source.getCreature().setGuiElement(null);
        source.setCreature(null);
    }
    
    public static void set_color (final GuiElement source) {
        if (Settings.debug_gui) Log.log("set_color ["+source.getName()+"]");
        if (source.hasProperty("set_color"))
            source.setColor((Color) source.getProperty("set_color"));
    }
    
    public static void savegame (final GuiElement source) {
        if (Settings.debug_gui) Log.log("savegame ["+source.getName()+"]");
        SavedStateFactory.save();
    }
    
}
