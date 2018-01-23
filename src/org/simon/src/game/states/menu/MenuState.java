/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.menu;

import java.io.File;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.data.save.SavedStateFactory;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.game.states.newgame.NewGameState;

/**
 *
 * @author emil.simon
 */
public class MenuState extends BasicGameState {
    
    public static final int ID = 2;
    
    public static final String CONTINUE_LABEL = "Continue";
    public static final String PLAY_LABEL = "Play New Game";
    public static final String INFO_LABEL = "About";
    public static final String QUIT_LABEL = "Exit Game";
    
    
    public static GuiController gui;

    
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gui = new GuiController ();
        String el_name;
        
        el_name = "cont_btn";
        GuiElement cont_btn = new GuiElement (el_name, gui, true, 0.33f, 0.2f, true, 0.34f, 0.1f, "ui/btn")
                .setText(CONTINUE_LABEL)
                .setFont("consolas", 32)
                .setOnClick("continue_game")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                .setVisible(false)
                ;
        gui.addElement(el_name, cont_btn);
        
        el_name = "play_btn";
        GuiElement play_btn = new GuiElement (el_name, gui, true, 0.33f, 0.4f, true, 0.34f, 0.1f, "ui/btn")
                .setText(PLAY_LABEL)
                .setFont("consolas", 32)
                .setOnClick("enter_state")
                .setProperty("enter_state", NewGameState.ID)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, play_btn);
        
        el_name = "info_btn";
        GuiElement info_btn = new GuiElement (el_name, gui, true, 0.33f, 0.6f, true, 0.34f, 0.1f, "ui/btn")
                .setText(INFO_LABEL)
                .setFont("consolas", 32)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, info_btn);
        
        el_name = "exit_btn";
        GuiElement exit_btn = new GuiElement (el_name, gui, true, 0.33f, 0.8f, true, 0.34f, 0.1f, "ui/btn")
                .setText(QUIT_LABEL)
                .setFont("consolas", 32)
                .setOnClick("exit")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, exit_btn);
        
        el_name = "overlay";
        GuiElement overlay = new GuiElement (el_name, gui, true, 0f, 0f, true, 1f, 1f, "ui/block")
                .setVisible(false)
                .setColor(0f,0f,0f,0f)
                .setProperty("fade_speed", .5f)
                .setProperty("start_a", 1f)
                .setProperty("start_text_a", 1f)
                .setProperty("fadein_callback", "enter_state&fadeout")
                .setProperty("fadeout_callback", "hide")
                .setProperty("enter_state", CombatState.ID)
                .setProperty("acceleration", 0f)
                .setLayer(100)
                ;
        gui.addElement(el_name, overlay);
    }
    
    
    
    @Override
    public void enter (GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        SharedState.updateStateId(MenuState.ID);
        
        File f = new File (SavedStateFactory.SAVED_STATE_FILE_PATH);
        gui.getElement("cont_btn").setVisible( f.exists() );
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
