/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.cardgallery;

import java.util.Comparator;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.gui.GuiController;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class CardGalleryState extends BasicGameState implements MouseListener {
    
    public enum CardGallerySubState {
        BROWSE, CLOSEUP
    }
    
    public static final int ID = 502;
    
    private float y_offset = 0f;
    private float max_y_offset = 0f;
    private final float Y_OFFSET_STEP = 0.1f;
    
    public static GuiController gui;
    public static GuiController closeup_gui;
    
    private static List<Card> card_list;
    
    private static CardGallerySubState substate;
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
            
        card_list = CardLibrary.getAllCards();
        card_list.sort((Card c1, Card c2) -> c1.getTotalPointCost() - c2.getTotalPointCost());
        
        substate = CardGallerySubState.BROWSE;
        
        String el_name;
        
        gui = new GuiController ();
        float card_x_margin = 0.04f * Settings.screen_width;
        float card_y_margin = 0.04f * Settings.screen_width;
        float card_width = 0.15f * Settings.screen_width;
        float card_height = (Card.STANDARD_CARD_HEIGHT / Card.STANDARD_CARD_WIDTH) * card_width;
        float total_card_width = card_width + card_x_margin;
        float total_card_height = card_height + card_y_margin;
        
        int cols = (int) Math.floor(Settings.screen_width / total_card_width);
        int rows = (int) Math.ceil(card_list.size() / cols);
        
        for (int row=0;row<rows;row++) {
            float y = card_y_margin + row*total_card_height;
            
            for (int col=0;col<cols;col++) {
                float x = card_x_margin + col*total_card_width;
                int index = (row * cols) + col;
                Card card = card_list.get(index);
                el_name = "card_el_"+index;
                GuiElement card_el = new GuiElement (el_name, gui, false, x, y, false, card_width, card_height, "")
                        .setCard(card)
                        .setColor(1f, 1f, 1f, 1f)
                        .setLayer(index)
                        .setOnClick("closeup")
                        ;
                gui.addElement(el_name, card_el);
            }
        }
        
        max_y_offset = Math.max (0f, rows * (card_height + card_y_margin) - Settings.screen_height);
        
        closeup_gui = new GuiController ();
        float scale = 2f;
        float actual_card_width = (card_width * scale);
        float actual_card_height = (card_height * scale);
        float x = Settings.screen_width/2f - actual_card_width/2f;
        float y = Settings.screen_height/2f - actual_card_height/2f;
        el_name = "card_closeup";
        GuiElement card_closeup = new GuiElement (el_name, closeup_gui, false, x, y, false, actual_card_width, actual_card_height, "")
                .setVisible(false)
                .setColor(Color.white)
                .setLayer(10)
                ;
        closeup_gui.addElement(el_name, card_closeup);
    }
    
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs, 0f, y_offset);
        closeup_gui.render(grphcs, 0, 0);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        gui.update(gc, sbg, dt, 0, (int) -y_offset);
        closeup_gui.update(gc, sbg, dt);
        
        if (gc.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) switchToBrowse();
        
        SharedState.update(gc, sbg, null);
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        gc.getInput().addMouseListener(this);
        SharedState.updateStateId(CardGalleryState.ID);
    }
    
    @Override
    public void mouseWheelMoved(int change) {
        if (substate.equals(CardGallerySubState.CLOSEUP)) return;
        
        float actual_change = change * Y_OFFSET_STEP;
        y_offset = Math.min(0f, y_offset + actual_change);
        y_offset = Math.max(y_offset, -max_y_offset);
    }
    
    
    
    public static void switchToCloseup (Card card) {
        GuiElement card_closeup = closeup_gui.getElement("card_closeup");
        card_closeup.setCard(card);
        card_closeup.setVisible(true);
        substate = CardGallerySubState.CLOSEUP;
    }
    
    public static void switchToBrowse () {
        GuiElement card_closeup = closeup_gui.getElement("card_closeup");
        card_closeup.setVisible(false);
        substate = CardGallerySubState.BROWSE;
    }
    
    public static void setCardList (List<Card> card_list) {
        CardGalleryState.card_list = card_list;
        switchToBrowse();
    }
    
}
