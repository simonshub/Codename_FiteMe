/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.cardcrafter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.gui.Gui;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceMgr;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class CardCrafterState extends BasicGameState {
    
    public static final int ID = 500;
    
    public static Gui gui;
    
    private static CardCrafterFrame frame = null;
    
    private static Card card;
    private static int current_image_index;
    private static List<String> card_images;
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
            
        card_images = ResourceMgr.getAllGraphicsStartingWith("cards/");
        Collections.sort(card_images);
        current_image_index = 0;
        
        card = new Card ();
        card.setName("");
        card.setAction("");
        card.setDescription("");
        card.setIcon(card_images.get(current_image_index));
        
        gui = new Gui ();
        String el_name;
        float scale = 3f;
        float card_width = Card.STANDARD_CARD_WIDTH * scale;
        float card_height = Card.STANDARD_CARD_HEIGHT * scale;
        float target_width = 32f * scale;
        float target_height = 32f * scale;
        
        el_name = "card_preview";
        GuiElement card_preview = new GuiElement (el_name, gui, false, Settings.screen_width/2f - card_width/2f, 0f, false, card_width, card_height, "")
                .setCard(card)
                .setColor(1f, 1f, 1f, 1f)
                ;
        gui.addElement(el_name, card_preview);
        
        el_name = "missile_preview_src";
        GuiElement missile_preview_src = new GuiElement (el_name, gui, false, 0f, Settings.screen_height - target_height, false, target_width, target_height, "")
                .setColor(0f, 0f, 1f, 1f)
                ;
        gui.addElement(el_name, missile_preview_src);
        
        el_name = "missile_preview_tar";
        GuiElement missile_preview_tar = new GuiElement (el_name, gui, false, Settings.screen_width - target_width, Settings.screen_height - target_height, false, target_width, target_height, "")
                .setColor(1f, 0f, 0f, 1f)
                ;
        gui.addElement(el_name, missile_preview_tar);
    }
    
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        gui.update(gc,sbg,dt);
        if (SharedState.update(gc,sbg,frame)) return;
        
        if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            rotateCurrentImageIndexLeft();
            card.setIcon(card_images.get(current_image_index));
        } else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            rotateCurrentImageIndexRight();
            card.setIcon(card_images.get(current_image_index));
        }
        
        if (frame.flag_prev) {
            rotateCurrentImageIndexLeft();
            card.setIcon(card_images.get(current_image_index));
        } else if (frame.flag_next) {
            rotateCurrentImageIndexRight();
            card.setIcon(card_images.get(current_image_index));
        } else if (frame.flag_save) {
            frame.flag_save = false;
            card.assignIdOverwrite(frame.getId());
            card.setAction(frame.getActionValue());
            
            if (Arrays.asList(CardLibrary.getLoadedCardPacks()).contains(frame.getCurrentCardPackName())) {
                CardLibrary.saveCardToPack(card, CardLibrary.getCardPackFilePath(frame.getCurrentCardPackName()));
                Log.log("Successfully saved card '"+card.getId()+"' to pack '"+frame.getCurrentCardPackName()+"'");
            } else {
                Log.err("Cannot save card '"+card.getId()+"' to pack '"+frame.getCurrentCardPackName()+"' because the pack was not loaded!");
            }
        }
        
        frame.setIconLabelValue(card_images.get(current_image_index));
        
        card.setDescription(frame.getDescriptionValue());
        card.silentSetAction(frame.getActionValue());
        card.setName(frame.getNameValue());
        card.setPointCost(PointTypeEnum.ATTACK, frame.getPointAttack());
        card.setPointCost(PointTypeEnum.DEFENCE, frame.getPointDefence());
        card.setPointCost(PointTypeEnum.AGILITY, frame.getPointAgility());
        card.setPointCost(PointTypeEnum.ARCANE, frame.getPointArcane());
        card.setPointCost(PointTypeEnum.DIVINE, frame.getPointDivine());
        card.setPointCost(PointTypeEnum.DEATH, frame.getPointDeath());
        card.setPointCost(PointTypeEnum.NATURE, frame.getPointNature());
        card.setIconBackground(frame.getColor());
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        
        if (frame == null) {
            frame = new CardCrafterFrame ();
        }
        
        frame.setVisible(true);
    }
    
    
    
    public void rotateCurrentImageIndexLeft () {
        current_image_index = current_image_index-1;
        
        if (current_image_index < 0)
            current_image_index += card_images.size();
        
        frame.flag_next = false;
        frame.flag_prev = false;
    }
    
    public void rotateCurrentImageIndexRight () {
        current_image_index = current_image_index+1;
        
        if (current_image_index >= card_images.size())
            current_image_index -= card_images.size();
        
        frame.flag_next = false;
        frame.flag_prev = false;
    }
    
}
