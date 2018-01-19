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
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.cards.CardPool;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiActionHandler;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.gui.TutorialStringLibrary;
import org.simon.src.game.states.SharedState;
import org.simon.src.utils.Log;

/**
 *
 * @author emil.simon
 */
public class CombatState extends BasicGameState {
    
    public static final int ID = 1;
    
    public static final String TURN_INDICATOR_SUFFIX = "'s Turn";
    
    public enum CombatSubState {
        PICK_CARD, PICK_TARGET, ENEMY_TURN
    }
    
    
    
    public static CombatSubState substate;
    
    public static GuiController gui;
    public static SpecialEffectSystem sfx;

    
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        substate = CombatSubState.PICK_CARD;
        
        sfx = new SpecialEffectSystem ();
        
        gui = new GuiController ();
        String el_name;
        
        CardPool test_deck = new CardPool (CardLibrary.getAllCards());
        List<Card> test_hand = test_deck.getRandomCards(5);
        
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
                .setProperty("acceleration", 0.01f)
                .setProperty("scale_limit", 1.25f)
                .setProperty("scale_speed", 0.15f)
                .setWhileHovered("scaleup")
                .setWhileUnhovered("scalebackdown")
                .setOnClick("printinfo")
                .setCard(CardLibrary.getCard("armored_shell"))
                .setVisible(false)
                ;
        gui.addElement(el_name, played_card);
        
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
                    .setCard(test_hand.get(i))
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
        
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
    }
    
    @Override
    public void enter (GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        SharedState.updateStateId(CombatState.ID);
        
        gui.getElement("background").setImage(GameplayManager.getCurrentLevelType().getBackground());
        gui.getElement("overlay").instantCall("fadeout");
        List<GuiElement> character_elements = gui.getElements("_ally_");
        Player.bindParty(character_elements);
        GameplayManager.checkWaveSpawn();
    }
    
    
    
    public static void endTurn () {
        Log.log("end_turn");
        GuiElement end_turn = gui.getElement("end_turn");
        end_turn.setImage("ui/end_turn_disabled");
        end_turn.setWhileHovered("scalebackdown");
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        GameplayManager.turnTick(sfx);
        substate = CombatSubState.ENEMY_TURN;
        GameplayManager.checkWaveSpawn();
        
        List<GuiElement> enemy_el_list = gui.getElements("_enemy_");
        for (GuiElement el : enemy_el_list) {
            if (el.getCreature()!=null && !el.getCreature().isDead()) {
                el.getCreature().restorePoints();
            }
        }
    }
    
    public static void startTurn () {
        Log.log("start_turn");
        GuiElement end_turn = gui.getElement("end_turn");
        end_turn.setImage("ui/end_turn");
        end_turn.setWhileHovered("scaleup");
        gui.getElement("turn_indicator").setText(GameplayManager.getCurrentOpponentText()+TURN_INDICATOR_SUFFIX);
        GameplayManager.turnTick(sfx);
        substate = CombatSubState.PICK_CARD;
        
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
    }
    
    public static Creature getCurrentCastingCreature () {
        return GameplayManager.getCurrentCastingCreature();
    }
    
    public static void setCurrentTurnCreature (GuiElement element) {
        if (element.getCreature() == null) {
            Log.err("Error while setting current turn creature to element '"+element.getName()+"'; element does not contain a creature");
        } else {
            GameplayManager.setCurrentCastingCreature(element.getCreature());
        }
    }
    
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs);
        sfx.render(grphcs);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        SharedState.update(gc, sbg, null);
        
        if (gc.getInput().isKeyPressed(Input.KEY_F)) {
            // F is for Filip :)
            Log.log("Rerolling creatures and hand...");
            reroll();
        } else if (gc.getInput().isKeyPressed(Input.KEY_R)) {
            sfx.reset();
        } 
        
        gui.update(gc,sbg,dt);
        sfx.update(dt);
        
        if (gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            GuiActionHandler.deselectcard(gui.getElement("played_card"));
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_RETURN)) {
            startTurn(); 
        }
    }
    
    public static void reroll () {
        List<GuiElement> elements;
//        GameplayManager.clearEnemies();
//        
//        // roll enemy creatures
//        elements = gui.getElements("creature_slot_enemy");
//        for (int i=0;i<elements.size();i++) {
//            Creature creature = new Creature (CreatureLibrary.getRandomCreature());
//            elements.get(i).setCreature(creature);
//            GameplayManager.addEnemy(creature);
//        }
        
        // roll player hand
        CardPool test_deck = new CardPool (CardLibrary.getAllCards());
        List<Card> test_hand = test_deck.getRandomCards(5);
        elements = gui.getElements("card_slot");
        for (int i=0;i<elements.size();i++) {
            elements.get(i).setCard(test_hand.get(i));
        }
        
    }
    
}
