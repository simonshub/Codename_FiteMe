/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.combat;

import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.cards.CardPool;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.gameplay.player.PlayerCharacter;
import org.simon.src.game.data.save.SavedStateFactory;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiActionHandler;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.gui.TutorialStringLibrary;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.menu.MenuState;
import org.simon.src.utils.Consts;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class CombatState extends BasicGameState {
    
    public static final int ID = 1;
    
    public static final String TURN_INDICATOR_SUFFIX = "'s Turn";
    
    public static final String CONTINUE_LABEL = "Continue";
    public static final String QUIT_LABEL = "Save & Exit to Main Menu";
    public static final String EXIT_LABEL = "Save & Exit to Desktop";
    
    public enum CombatSubState {
        PICK_CARD, PICK_TARGET, ENEMY_TURN, GAME_OVER
    }
    
    
    
    public static boolean paused = false;
    public static boolean next_level = false;
    
    public static CombatSubState substate;
    
    public static GuiController gui;
    public static GuiController menu_gui;
    public static GuiController next_lvl_gui;
    public static SpecialEffectSystem sfx;

    
    
    @Override
    public int getID() {
        return ID;
    }
    
    
    
    public void initMenuGui () {
        menu_gui = new GuiController ();
        
        String el_name;
        
        el_name = "cont_btn";
        GuiElement cont_btn = new GuiElement (el_name, menu_gui, true, 0.25f, 0.3f, true, 0.5f, 0.1f, "ui/btn")
                .setText(CONTINUE_LABEL)
                .setFont("consolas", 32)
                .setOnClick("unpause_combatstate")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setLayer(10)
                ;
        menu_gui.addElement(el_name, cont_btn);
        
        el_name = "quit_btn";
        GuiElement quit_btn = new GuiElement (el_name, menu_gui, true, 0.25f, 0.5f, true, 0.5f, 0.1f, "ui/btn")
                .setText(QUIT_LABEL)
                .setFont("consolas", 32)
                .setOnClick("enter_state&savegame")
                .setProperty("enter_state", MenuState.ID)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setLayer(10)
                ;
        menu_gui.addElement(el_name, quit_btn);
        
        el_name = "exit_btn";
        GuiElement exit_btn = new GuiElement (el_name, menu_gui, true, 0.25f, 0.7f, true, 0.5f, 0.1f, "ui/btn")
                .setText(EXIT_LABEL)
                .setFont("consolas", 32)
                .setOnClick("savegame&exit")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setLayer(10)
                ;
        menu_gui.addElement(el_name, exit_btn);
        
        el_name = "underlay";
        GuiElement underlay = new GuiElement (el_name, menu_gui, true, 0f, 0f, true, 1f, 1f, "ui/block")
                .setColor(new Color (0f, 0f, 0f, 0.5f))
                .setLayer(5)
                ;
        menu_gui.addElement(el_name, underlay);
    }
    
    public static void generateNextLvlGui () {
        next_lvl_gui = new GuiController ();
        
        String el_name;
        
        el_name = "underlay";
        GuiElement underlay = new GuiElement (el_name, next_lvl_gui, true, 0f, 0f, true, 1f, 1f, "ui/block")
                .setLayer(-5)
                .setColor(0f,0f,0f,0f)
                .setProperty("fade_speed", .75f)
                .setProperty("fadeout_callback", "unlock_combatstate")
                .setProperty("start_a", 1f)
                .setProperty("start_text_a", 1f)
                ;
        next_lvl_gui.addElement(el_name, underlay);
        
        el_name = "title";
        GuiElement title = new GuiElement (el_name, next_lvl_gui, true, 0f, 0f, true, 1f, 0.15f, "ui/block")
                .setLayer(5)
                .setColor(0f,0f,0f,0f)
                .setTextColor(1f,1f,1f,0f)
                .setFont("consolas", 48)
                .setText("Level Finished")
                .setProperty("fade_speed", .5f)
                .setProperty("start_a", 0f)
                .setProperty("start_text_a", 1f)
                ;
        next_lvl_gui.addElement(el_name, title);
        
        List<PlayerCharacter> chars = Player.getParty();
        float x = 0.04f;
        float y = 0.35f;
        float width = 0.2f;
        float height = 0.5f;
        
        for (int index=0;index<chars.size();index++) {
            el_name = "char_" + index;
            GuiElement char_background = new GuiElement (el_name, next_lvl_gui, true, x, y, true, width, height, "ui/box")
                    .setLayer(2)
                    .setColor(1f,1f,1f,0f)
                    .setProperty("fade_speed", .5f)
                    .setProperty("start_a", 1f)
                    .setProperty("start_text_a", 1f)
                    ;
            next_lvl_gui.addElement(el_name, char_background);

            float portrait_x_margin = 0.1f * width;
            float portrait_y_margin = 0.1f * height;
            float portrait_width = width - 2*portrait_x_margin;
            float portrait_height = height - 2*portrait_y_margin;

            el_name = "char_portrait_" + index;
            GuiElement char_portrait = new GuiElement (el_name, next_lvl_gui, true, x + portrait_x_margin, y + portrait_y_margin, true, portrait_width, portrait_height)
                    .setLayer(5)
                    .setImage(chars.get(index).getCharacterClass().getPortrait())
                    .setColor(1f,1f,1f,0f)
                    .setProperty("fade_speed", .5f)
                    .setProperty("start_a", 1f)
                    .setProperty("start_text_a", 1f)
                    ;
            
            if (chars.get(index).getCreature().isDead()) {
                String death_indicator_id = "dead_indicator_"+index;
                char_portrait.setColor(.5f,.5f,.5f,1f);
                float dead_indicator_size = portrait_width * Settings.screen_width;
                float dead_indicator_y_offset = ( (portrait_height * Settings.screen_height) - (dead_indicator_size * 1.5f) ) / Settings.screen_height;
                GuiElement dead_indicator = new GuiElement (death_indicator_id, next_lvl_gui, true, x + portrait_x_margin, y + portrait_y_margin + dead_indicator_y_offset, false, dead_indicator_size, dead_indicator_size)
                        .setLayer(10)
                        .setImage("ui/dead")
                        .setColor(1f,1f,1f,0f)
                        .setProperty("fade_speed", .5f)
                        .setProperty("start_a", 1f)
                        .setProperty("start_text_a", 1f)
                        ;
                next_lvl_gui.addElement(death_indicator_id, dead_indicator);
            } else {
                int hp_gain = chars.get(index).getCharacterClass().getHealthPerLevel();
                List<PointTypeEnum> new_points = chars.get(index).getCharacterClass().getNewPointsForLevel( chars.get(index).getLevel() + 1 );
                
                String lvlup_el_name = "stats_hp_"+index;
                GuiElement stats_hp = new GuiElement (lvlup_el_name, next_lvl_gui, true, x, 0.2f, true, width, 0.1f, "stats/health")
                        .setRenderScaled(false)
                        .setLayer(5)
                        .setColor(1f,1f,1f,0f)
                        .setTextColor(1f,1f,1f,0f)
                        .setFont("consolas", 24)
                        .setText("+ "+hp_gain+"        ")
                        .setProperty("fade_speed", .5f)
                        .setProperty("start_a", 1f)
                        .setProperty("start_text_a", 1f)
                        ;
                next_lvl_gui.addElement(lvlup_el_name, stats_hp);
                
                for (int i=0;i<new_points.size();i++) {
                    float pt_sz = PointTypeEnum.NOMINAL_ICON_SIZE * 0.7f;
                    float pt_x = (x*Settings.screen_width) + ( (portrait_width*Settings.screen_width) / 2f ) - ( (new_points.size()*pt_sz) / 2f ) + (pt_sz*i) + (pt_sz/2f);
                    float pt_y = 0.3f*Settings.screen_height;
                    
                    Color bkg_col = new_points.get(i).color;
                    bkg_col.a = 0f;
                    
                    lvlup_el_name = "stats_pt_"+index+"_"+i+"_background";
                    GuiElement stats_pt_bkg = new GuiElement (lvlup_el_name, next_lvl_gui, false, pt_x, pt_y, false, pt_sz, pt_sz, PointTypeEnum.POINT_ICON_BACKGROUND_NAME)
                            .setLayer(5)
                            .setColor(bkg_col)
                            .setProperty("fade_speed", .5f)
                            .setProperty("start_a", 1f)
                            .setProperty("start_text_a", 1f)
                            ;
                    next_lvl_gui.addElement(lvlup_el_name, stats_pt_bkg);
                    
                    lvlup_el_name = "stats_pt_"+index+"_"+i;
                    GuiElement stats_pt = new GuiElement (lvlup_el_name, next_lvl_gui, false, pt_x, pt_y, false, pt_sz, pt_sz, new_points.get(i).icon_name)
                            .setLayer(6)
                            .setColor(1f,1f,1f,0f)
                            .setProperty("fade_speed", .5f)
                            .setProperty("start_a", 1f)
                            .setProperty("start_text_a", 1f)
                            ;
                    next_lvl_gui.addElement(lvlup_el_name, stats_pt);
                }
            }
            
            next_lvl_gui.addElement(el_name, char_portrait);
            x += 0.24f;
        }
        
        el_name = "next_level_btn";
        GuiElement next_level_btn = new GuiElement (el_name, next_lvl_gui, true, 0.2f, 0.9f, true, 0.6f, 0.1f, "ui/btn")
                .setFont("consolas", 32)
                .setText("Onwards!")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setOnClick("next_level")
                .setColor(1f,1f,1f,0f)
                .setTextColor(1f,1f,1f,0f)
                .setProperty("fade_speed", .5f)
                .setProperty("acceleration", 0f)
                .setProperty("start_a", 1f)
                .setProperty("start_text_a", 1f)
                .setLayer(1)
                ;
        next_lvl_gui.addElement(el_name, next_level_btn);
    }
    
    public void initGui () {
        gui = new GuiController ();
        
        String el_name;
        
        el_name = "turn_indicator";
        GuiElement turn_indicator = new GuiElement (el_name, gui, true, 0.35f, 0f, true, 0.3f, 0.05f, "ui/btn_alt")
                .setTextColor(1f, 1f, 1f, 1f)
                .setFont("consolas", 20)
                .setColor(0.8f,0.8f,0.8f,1f);
        gui.addElement(el_name, turn_indicator);
        
        el_name = "tutorial_label";
        GuiElement tutorial_label = new GuiElement (el_name, gui, true, 0.1f, 0.05f, true, 0.8f, 0.1f, "img1")
                .setText(TutorialStringLibrary.PICK_A_CARD)
                .setLayer(2)
                .setColor(0.5f,0.5f,0.5f,0.5f)
                .setTextColor(1f, 1f, 1f, 1f)
                .setFont("consolas", 25)
                .setProperty("fade_speed", 2.0f)
                .setProperty("set_text", TutorialStringLibrary.PICK_A_CARD)
                .setProperty("set_onclick", "fadeout")
                .setProperty("fadeout_callback", "clear_onclick")
                .setProperty("fadein_callback", "set_onclick")
                .setOnClick("fadeout")
                ;
        gui.addElement(el_name, tutorial_label);
        
        float creature_el_width = Creature.DEFAULT_CREATURE_WIDTH;
        float creature_el_height = Creature.DEFAULT_CREATURE_HEIGHT;
        float creature_el_x = 0.125f;
        float creature_el_y = 0.1f;
        el_name = "creature_slot";
        for (int i=0;i<5;i++) {
            creature_el_x += 0.01f; // margin
            String el_name_complete = el_name+"_enemy_"+String.valueOf(i);
            GuiElement creature_slot = new GuiElement (el_name_complete, gui, true, creature_el_x, creature_el_y, true, creature_el_width, creature_el_height, "")
                    .setColor(Color.white)
                    .setLayer(1)
                    .setCreature(null)
                    .setOnClick("selecttarget")
                    .setProperty("fade_speed", .75f)
                    ;
            gui.addElement(el_name_complete, creature_slot);
            creature_el_x += creature_el_width;
            creature_el_x += 0.01f; // margin
        }
        
        creature_el_x = 0.19f;
        creature_el_y = 0.45f;
        el_name = "creature_slot";
        for (int i=0;i<4;i++) {
            creature_el_x += 0.01f; // margin
            GuiElement creature_slot = new GuiElement (el_name+"_ally_"+String.valueOf(i), gui, true, creature_el_x, creature_el_y, true, creature_el_width, creature_el_height, "")
                    .setColor(Color.white)
                    .setLayer(1)
                    .setCreature(null)
                    .setOnClick("selecttarget")
                    .setProperty("fade_speed", .75f)
                    ;
            gui.addElement(el_name+"_ally_"+String.valueOf(i), creature_slot);
            creature_el_x += creature_el_width;
            creature_el_x += 0.01f; // margin
        }
        
        el_name = "played_card";
        GuiElement played_card = new GuiElement (el_name, gui, true, 0.85f, 0.3f, true, 0.15f, 0.3f, "")
                .setColor(0.4f,0.8f,0.4f,1f)
                .setFont("consolas", 45)
                .setLayer(3)
                .setCard(CardLibrary.getCard("armored_shell"))
                .setVisible(false)
                ;
        gui.addElement(el_name, played_card);
        
        el_name = "enemy_played_card";
        GuiElement enemy_played_card = new GuiElement (el_name, gui, true, 0f, 0.3f, true, 0.15f, 0.3f, "")
                .setColor(0.4f,0.8f,0.4f,1f)
                .setFont("consolas", 45)
                .setLayer(3)
                .setCard(CardLibrary.getCard("armored_shell"))
                .setVisible(false)
                ;
        gui.addElement(el_name, enemy_played_card);
        
        el_name = "player_hand";
        GuiElement player_hand = new GuiElement (el_name, gui, true, 0.1f, 0.78f, true, 0.8f, 0.22f, "ui/box_noshadow")
                ;
        gui.addElement(el_name, player_hand);
        
        float card_el_width = 0.1f;
        float card_el_height = 0.2f;
        float card_el_x = 0.18f;
        float card_el_y = 0.8f;
        el_name = "card_slot";
        for (int i=0;i<5;i++) {
            String slot_name = el_name+"_"+String.valueOf(i);
            card_el_x += 0.01f; // margin
            GuiElement card_slot = new GuiElement (slot_name, gui, true, card_el_x, card_el_y, true, card_el_width, card_el_height, "")
                    .setColor(0.5f,0.6f,0.4f,1f)
                    .setLayer(2)
                    .setProperty("acceleration", 0f)
                    .setProperty("float_limit", 48f)
                    .setProperty("float_speed", 0.3f)
                    .setProperty("scale_limit", 1.18f)
                    .setProperty("scale_speed", 2f)
                    .setProperty("card_slot", i)
                    .setProperty("parent", gui)
                    .setWhileHovered("scaleup")
                    .setWhileUnhovered("scalebackdown")
                    .setOnClick("selectcard")
                    .setCard(CardLibrary.getCard("test_card"))
                    ;
            gui.addElement(slot_name, card_slot);
            card_el_x += card_el_width;
        }
        
        card_el_x += 0.045f; // margin
        card_el_y = 0.84f;
        card_el_width = 0.1f;
        el_name = "end_turn";
        GuiElement end_turn = new GuiElement (el_name, gui, true, card_el_x, card_el_y, true, card_el_width, card_el_width, "ui/end_turn")
                .setColor(Color.white)
                .setOnClick("endturn")
                .setLayer(1)
                .setProperty("acceleration", 0.05f)
                .setProperty("scale_limit", 1.1f)
                .setProperty("scale_speed", 2f)
                .setWhileHovered("scaleup")
                .setWhileUnhovered("scalebackdown")
                ;
        // manually adapt the end turn button width, to be equal to it's height (a square)
        end_turn.base_width = end_turn.base_height;
        end_turn.width = end_turn.height;
        gui.addElement(el_name, end_turn);
        
        el_name = "background";
        GuiElement background = new GuiElement (el_name, gui, true, 0f, 0f, true, 1f, 1f)
                .setLayer(-2)
                ;
        gui.addElement(el_name, background);
        
        el_name = "overlay";
        GuiElement overlay = new GuiElement (el_name, gui, true, 0f, 0f, true, 1f, 1f, "ui/block")
                .setVisible(true)
                .setColor(0f,0f,0f,1f)
                .setProperty("fade_speed", .5f)
                .setProperty("fadeout_callback", "hide")
                .setProperty("acceleration", 0f)
                .setLayer(1000)
                ;
        gui.addElement(el_name, overlay);
        
        el_name = "gameover_score";
        GuiElement gameover_score = new GuiElement (el_name, gui, true, 0f, 0.70f, true, 1f, 0.05f, "ui/block")
                .setVisible(false)
                .setColor(0f,0f,0f,0f)
                .setFont("consolas", Consts.DEFAULT_FONT_SIZE)
                .setText("Your score: ")
                .setTextColor(GameplayManager.GAMEOVER_COLOR)
                .setProperty("fade_speed", .5f)
                .setProperty("acceleration", 0f)
                .setLayer(1002)
                ;
        gui.addElement(el_name, gameover_score);
        
        el_name = "gameover_hint";
        GuiElement gameover_hint = new GuiElement (el_name, gui, true, 0f, 0.75f, true, 1f, 0.05f, "ui/block")
                .setVisible(false)
                .setColor(0f,0f,0f,0f)
                .setFont("consolas", Consts.DEFAULT_FONT_SIZE)
                .setText("Press [ ESC ] to exit to main menu . . .")
                .setTextColor(GameplayManager.GAMEOVER_COLOR)
                .setProperty("fade_speed", .5f)
                .setProperty("acceleration", 0f)
                .setLayer(1002)
                ;
        gui.addElement(el_name, gameover_hint);
    }
    
    
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        substate = CombatSubState.PICK_CARD;
        sfx = new SpecialEffectSystem ();
        paused = false;
        
        initGui();
        initMenuGui();
    }
    
    @Override
    public void enter (GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        SharedState.updateStateId(CombatState.ID);
        
        CombatState.paused = false;
        if (GameplayManager.isNewGame()) {
            GameplayManager.setIsNewGame(false);
            List<GuiElement> creature_slots = gui.getElements("creature_slot");
            
            for (GuiElement creature_slot : creature_slots) {
                creature_slot.setCreature(null);
            }
            
            GameplayManager.clearEnemies();
            GameplayManager.spawnWave();
            
            List<GuiElement> party_slots = gui.getElements("_ally_");
            Player.bindParty(party_slots);
            drawNewHand();
            
            for (GuiElement creature_slot : creature_slots) {
                if (creature_slot.getCreature() != null && !creature_slot.getCreature().isDead())
                    creature_slot.instantCall("fadein");
            }
            
            substate = CombatSubState.PICK_CARD;
        }
        
        refreshBoardState();
    }
    
    
    
    public static void refreshBoardState () {
        paused = false;
        next_level = false;
        GuiActionHandler.deselectcard(gui.getElement("played_card"));
        
        gui.getElement("background").setImage(GameplayManager.getCurrentLevelType().getBackground());
        gui.getElement("overlay").instantCall("fadeout");
        gui.getElement("overlay").setText("");
        gui.getElement("gameover_score").instantCall("fadeout");
        gui.getElement("gameover_hint").instantCall("fadeout");
        gui.getElement("gameover_score").setVisible(false);
        gui.getElement("gameover_hint").setVisible(false);
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        
        GuiElement end_turn = gui.getElement("end_turn");
        end_turn.setImage("ui/end_turn");
        end_turn.setWhileHovered("scaleup");
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        
        List<GuiElement> creature_slots = gui.getElements("creature_slot");
        for (GuiElement el : creature_slots) {
            if (el.getCreature()!=null && !el.getCreature().isDead())
                el.instantCall("fadein");
        }
        
        GuiElement enemy_played_card = gui.getElement("enemy_played_card");
        enemy_played_card.setVisible(false);
    }
    
    
    
    public static Creature getCurrentCastingCreature () {
        return GameplayManager.getCurrentCastingCreature();
    }
    
    public static void setCurrentTurnCreature (GuiElement element) {
        if (element==null) {
            GameplayManager.setCurrentCastingCreature(null);
            return;
        }
        
        if (element.getCreature() == null) {
            Log.err("Error while setting current turn creature to element '"+element.getName()+"'; element does not contain a creature");
        } else if (element.getCreature().isDisabled()) {
            Log.err("Error while setting current turn creature to element '"+element.getName()+"'; creature is disabled");
        } else if (element.getCreature().isDead()) {
            Log.err("Error while setting current turn creature to element '"+element.getName()+"'; creature is dead");
        } else {
            GameplayManager.setCurrentCastingCreature(element.getCreature());
        }
    }
    
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs);
        sfx.render(grphcs);
        if (!next_level && paused) menu_gui.render(grphcs);
        if (next_level) next_lvl_gui.render(grphcs);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        SharedState.update(gc, sbg, null);
        
        if (!paused && !next_level) {
            GameplayManager.aiUpdate();

            if (gc.getInput().isKeyPressed(Input.KEY_F)) {
                // F is for Filip :)
                Log.log("Re-drawing player hand...");
                drawNewHand();
            } else if (gc.getInput().isKeyPressed(Input.KEY_R)) {
                sfx.reset();
            } 

            gui.update(gc,sbg,dt);
            sfx.update(dt);

            if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
                if (GameplayManager.allAlliesDead())
                    sbg.enterState(MenuState.ID);
                else
                    paused = true;
            }

            if (gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                GuiActionHandler.deselectcard(gui.getElement("played_card"));
            }
        } else if (paused && !next_level) {
            menu_gui.update(gc, sbg, dt);
            
            if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
                if (GameplayManager.allAlliesDead())
                    sbg.enterState(MenuState.ID);
                else
                    paused = false;
            }
        } else {
            next_lvl_gui.update(gc, sbg, dt);
            gui.update(gc,sbg,dt);
            sfx.update(dt);
        }
    }
    
    
    
    public static void endTurn () {
        Log.log("end_turn");
        GuiElement end_turn = gui.getElement("end_turn");
        end_turn.setImage("ui/end_turn_disabled");
        end_turn.setWhileHovered("scalebackdown");
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        
        List<GuiElement> enemy_el_list = gui.getElements("_enemy_");
        for (GuiElement el : enemy_el_list) {
            if (el.getCreature()!=null && !el.getCreature().isDead()) {
                el.getCreature().restorePoints();
            }
        }
        
        setCurrentTurnCreature(null);
        substate = CombatSubState.ENEMY_TURN;
        GameplayManager.checkWaveSpawn();
        GameplayManager.turnTick(sfx);
    }
    
    public static void startTurn () {
        if (GameplayManager.allAlliesDead()) return;
        
        Log.log("start_turn");
        GuiElement end_turn = gui.getElement("end_turn");
        end_turn.setImage("ui/end_turn");
        end_turn.setWhileHovered("scaleup");
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        
        GuiElement enemy_played_card = gui.getElement("enemy_played_card");
        enemy_played_card.setVisible(false);
        
        List<GuiElement> card_el_list = gui.getElements("card_slot");
        for (GuiElement el : card_el_list) {
            el.setCardPlayed(false);
        }
        
        List<GuiElement> char_el_list = gui.getElements("_ally_");
        for (GuiElement el : char_el_list) {
            if (el.getCreature()!=null && !el.getCreature().isDead()) {
                el.getCreature().restorePoints();
            }
        }
        
        substate = CombatSubState.PICK_CARD;
        drawNewHand();
        GameplayManager.turnTick(sfx);
        SavedStateFactory.save();
    }
    
    public static void drawNewHand () {
        CardPool deck = Player.getDeck();
        List<Card> new_hand = deck.getRandomCards(5);
        List<GuiElement> elements = gui.getElements("card_slot");
        for (int i=0;i<elements.size();i++) {
            elements.get(i).setCard(new_hand.get(i));
        }
        
    }
    
    public static void gameover () {
        if (ResourceManager.hasSound("gameover")) ResourceManager.getSound("gameover").play();
        
        GuiElement overlay = gui.getElement("overlay");
        overlay.setVisible(true);
        overlay.setFont(Consts.DEFAULT_FONT, 64);
        overlay.setText(GameplayManager.GAMEOVER_TEXT);
        overlay.setTextColor(GameplayManager.GAMEOVER_COLOR);
        overlay.instantCall("fadein");
        
        GuiElement gameover_hint = gui.getElement("gameover_hint");
        gameover_hint.setVisible(true);
        gameover_hint.instantCall("fadein");
        
        GuiElement gameover_score = gui.getElement("gameover_score");
        gameover_score.setVisible(true);
        gameover_score.setText("Your score: "+Player.getScore());
        gameover_score.instantCall("fadein");
        
        substate = CombatSubState.GAME_OVER;
        SavedStateFactory.delete();
    }
    
}
