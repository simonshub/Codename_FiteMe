/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.newgame;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.gui.Gui;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;

/**
 *
 * @author emil.simon
 */
public class NewGameState extends BasicGameState {
    
    public static final int ID = 3;
    
    public static final String TITLE_LABEL = "Choose your party";
    
    
    public static Gui gui;

    
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gui = new Gui ();
        String el_name;
        
        el_name = "title";
        GuiElement title = new GuiElement (el_name, gui, true, 0.2f, 0f, true, 0.6f, 0.1f, "ui/btn")
                .setFont("consolas", 32f)
                .setText(TITLE_LABEL)
                ;
        gui.addElement(el_name, title);
        
        addCharacterPickerGui("char_picker_0", 0.04f, 0.2f, 0.2f, 0.4f);
        addCharacterPickerGui("char_picker_1", 0.28f, 0.2f, 0.2f, 0.4f);
        addCharacterPickerGui("char_picker_2", 0.52f, 0.2f, 0.2f, 0.4f);
        addCharacterPickerGui("char_picker_3", 0.76f, 0.2f, 0.2f, 0.4f);
    }
    
    public void addCharacterPickerGui (String id, float x, float y, float width, float height) {
        
        String el_name = id + "_background";
        GuiElement char_picker_background = new GuiElement (el_name, gui, true, x, y, true, width, height, "ui/box")
                ;
        
        gui.addElement(el_name, char_picker_background);
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
