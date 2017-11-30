/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.states.creaturecrafter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.game.gui.Gui;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public class CreatureCrafterState extends BasicGameState {
    
    public static final int ID = 501;
    
    public static Gui gui;
    
    private static CreatureCrafterFrame frame = null;
    
    private static Creature creature;
    private static int current_image_index;
    private static List<String> creature_images;
    
    @Override
    public int getID() {
        return ID;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
            
        creature_images = ResourceManager.getAllGraphicsStartingWith("creatures/");
        Collections.sort(creature_images);
        current_image_index = 0;
        
        creature = new Creature ();
        creature.setName("");
        creature.setPoints(new HashMap<> ());
        creature.setIcon(creature_images.get(current_image_index));
        
        gui = new Gui ();
        String el_name;
        float scale = 2f;
        float creature_width = Creature.DEFAULT_CREATURE_WIDTH * scale * Settings.screen_width;
        float creature_height = Creature.DEFAULT_CREATURE_HEIGHT * scale * Settings.screen_height;
        
        el_name = "creature_preview";
        GuiElement creature_preview = new GuiElement (el_name, gui, false, Settings.screen_width/2f - creature_width/2f, Settings.screen_height/2f - creature_height/2f, false, creature_width, creature_height, "")
                .setCreatures(creature)
                .setColor(1f, 1f, 1f, 1f)
                ;
        gui.addElement(el_name, creature_preview);
    }
    
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        gui.render(grphcs);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int dt) throws SlickException {
        gui.update(gc,sbg,dt);
        if (SharedState.update(gc,sbg,frame)) return;
        
        if (gc.getInput().isKeyPressed(Input.KEY_C) && gc.getInput().isKeyDown(Input.KEY_LCONTROL)) {
            sbg.enterState(CombatState.ID);
            if (frame!=null) frame.setVisible(false);
            return;
        }
        
        if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
            rotateCurrentImageIndexLeft();
            creature.setIcon(creature_images.get(current_image_index));
        } else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
            rotateCurrentImageIndexRight();
            creature.setIcon(creature_images.get(current_image_index));
        }
        
        if (frame.flag_prev) {
            rotateCurrentImageIndexLeft();
            creature.setIcon(creature_images.get(current_image_index));
        } else if (frame.flag_next) {
            rotateCurrentImageIndexRight();
            creature.setIcon(creature_images.get(current_image_index));
        } else if (frame.flag_save) {
            frame.flag_save = false;
            creature.setName(frame.getNameValue());
            creature.assignIdOverwrite(frame.getId());
            
            if (Arrays.asList(CardLibrary.getLoadedCardPacks()).contains(frame.getCurrentCreaturePackName())) {
                CreatureLibrary.saveCreatureToPack(creature, CreatureLibrary.getCreaturePackFilePath(frame.getCurrentCreaturePackName()));
                Log.log("Successfully saved creature '"+creature.getId()+"' to pack '"+frame.getCurrentCreaturePackName()+"'");
            } else {
                Log.err("Cannot save creature '"+creature.getId()+"' to pack '"+frame.getCurrentCreaturePackName()+"' because the pack was not loaded!");
            }
        }
        
        frame.setIconLabelValue(creature_images.get(current_image_index));
        
        HashMap<PointTypeEnum, Integer> point_pool = new HashMap<> ();
        point_pool.put(PointTypeEnum.ATTACK, frame.getPointAttack());
        point_pool.put(PointTypeEnum.DEFENCE, frame.getPointDefence());
        point_pool.put(PointTypeEnum.AGILITY, frame.getPointAgility());
        point_pool.put(PointTypeEnum.ARCANE, frame.getPointArcane());
        point_pool.put(PointTypeEnum.DIVINE, frame.getPointDivine());
        point_pool.put(PointTypeEnum.DEATH, frame.getPointDeath());
        point_pool.put(PointTypeEnum.NATURE, frame.getPointNature());
        creature.setPoints(point_pool);
    }
    
    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.enter(gc, sbg);
        
        if (frame == null) {
            frame = new CreatureCrafterFrame ();
        }
        
        frame.setVisible(true);
    }
    
    
    
    public void rotateCurrentImageIndexLeft () {
        current_image_index = current_image_index-1;
        
        if (current_image_index < 0)
            current_image_index += creature_images.size();
        
        frame.flag_next = false;
        frame.flag_prev = false;
    }
    
    public void rotateCurrentImageIndexRight () {
        current_image_index = current_image_index+1;
        
        if (current_image_index >= creature_images.size())
            current_image_index -= creature_images.size();
        
        frame.flag_next = false;
        frame.flag_prev = false;
    }
    
}
