/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.newgame.NewGameState;

/**
 *
 * @author emil.simon
 */
public class MenuState extends BasicGameState {
    
    public static final int ID = 2;
    
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
        
        el_name = "play_btn";
        GuiElement play_btn = new GuiElement (el_name, gui, true, 0.33f, 0.3f, true, 0.34f, 0.1f, "ui/btn")
                .setText(PLAY_LABEL)
                .setFont("consolas", 32f)
                .setOnClick("enter_state")
                .setProperty("enter_state", NewGameState.ID)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, play_btn);
        
        el_name = "info_btn";
        GuiElement info_btn = new GuiElement (el_name, gui, true, 0.33f, 0.5f, true, 0.34f, 0.1f, "ui/btn")
                .setText(INFO_LABEL)
                .setFont("consolas", 32f)
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, info_btn);
        
        el_name = "exit_btn";
        GuiElement exit_btn = new GuiElement (el_name, gui, true, 0.33f, 0.7f, true, 0.34f, 0.1f, "ui/btn")
                .setText(QUIT_LABEL)
                .setFont("consolas", 32f)
                .setOnClick("exit")
                .setOnHover("hover_img")
                .setOnUnhover("unhover_img")
                .setProperty("hover_img", "ui/btn_hover")
                ;
        gui.addElement(el_name, exit_btn);
    }
    
    
    
    @Override
    public void enter (GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        SharedState.updateStateId(MenuState.ID);
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
