/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.newgame;

import java.util.List;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class NewGameState extends BasicGameState {
    
    public static final int ID = 3;
    
    public static final String TITLE_LABEL = "Choose your party";
    public static final String START_LABEL = "Start Game";
    
    public static GuiController gui;

    
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gui = new GuiController ();
        String el_name;
        
        el_name = "title";
        GuiElement title = new GuiElement (el_name, gui, true, 0.2f, 0f, true, 0.6f, 0.1f, "ui/btn_alt")
                .setFont("consolas", 32f)
                .setText(TITLE_LABEL)
                ;
        gui.addElement(el_name, title);
        
        addCharacterPickerGui(0, 0.04f, 0.2f, 0.2f, 0.5f);
        addCharacterPickerGui(1, 0.28f, 0.2f, 0.2f, 0.5f);
        addCharacterPickerGui(2, 0.52f, 0.2f, 0.2f, 0.5f);
        addCharacterPickerGui(3, 0.76f, 0.2f, 0.2f, 0.5f);
        
        el_name = "start_btn";
        GuiElement start_btn = new GuiElement (el_name, gui, true, 0.2f, 0.9f, true, 0.6f, 0.1f, "ui/btn")
                .setFont("consolas", 32f)
                .setText(START_LABEL)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setOnClick("start_new_game")
                .setProperty("enter_state", CombatState.ID);
                ;
        gui.addElement(el_name, start_btn);
    }
    
    public void addCharacterPickerGui (int index, float x, float y, float width, float height) {
        PlayerCharacterClass initial_selection = GameplayManager.getAllPlayerCharacterClassesCycleList().get(index);
        String el_name;
        
        el_name = "char_picker_" + index + "_background";
        GuiElement char_picker_background = new GuiElement (el_name, gui, true, x, y, true, width, height, "ui/box")
                ;
        gui.addElement(el_name, char_picker_background);
        
        float portrait_x_margin = 0.1f * width;
        float portrait_y_margin = 0.1f * height;
        float portrait_width = width - 2*portrait_x_margin;
        float portrait_height = height - 2*portrait_y_margin;
        
        el_name = "char_picker_" + index + "_portrait";
        GuiElement char_picker_portrait = new GuiElement (el_name, gui, true, x + portrait_x_margin, y + portrait_y_margin, true, portrait_width, portrait_height)
                .setLayer(5)
                .setImage(initial_selection.getPortrait())
                ;
        gui.addElement(el_name, char_picker_portrait);
        
        float prev_next_btn_y_margin = 0.2f * height;
        float prev_next_btn_size = 0.15f * width;
        
        el_name = "char_picker_" + index + "_prev_btn";
        GuiElement char_picker_prev_btn = new GuiElement (el_name, gui, true, x + portrait_x_margin, y + height - prev_next_btn_y_margin,
            false, prev_next_btn_size*Settings.screen_width, prev_next_btn_size*Settings.screen_width, "ui/left")
                .setLayer(10)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/left_hover")
                ;
        gui.addElement(el_name, char_picker_prev_btn);
        
        el_name = "char_picker_" + index + "_next_btn";
        GuiElement char_picker_next_btn = new GuiElement (el_name, gui, true, x + width - portrait_x_margin - prev_next_btn_size, y + height - prev_next_btn_y_margin,
            false, prev_next_btn_size*Settings.screen_width, prev_next_btn_size*Settings.screen_width, "ui/right")
                .setLayer(10)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/right_hover")
                ;
        gui.addElement(el_name, char_picker_next_btn);
        
        float label_height = 0.1f * height;
        
        el_name = "char_picker_" + index + "_label";
        GuiElement char_picker_label = new GuiElement (el_name, gui, true, x, y + height - label_height, true, width, label_height, "ui/box")
                .setLayer(8)
                .setFont("consolas", 24f)
                .setText(initial_selection.getName())
                ;
        gui.addElement(el_name, char_picker_label);
    }
    
    
    
    @Override
    public void enter (GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        SharedState.updateStateId(NewGameState.ID);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        SharedState.update(gc, sbg, null);
        gui.update(gc,sbg,dt);
    }
    
}
