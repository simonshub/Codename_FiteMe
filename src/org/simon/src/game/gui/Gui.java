/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;

/**
 *
 * @author emil.simon
 */
public final class Gui {
    
    public static int CLICK_LOCK_DURATION = 200;
    
    public static float FLOATING_TEXT_ELEMENT_WIDTH = 1f;
    public static float FLOATING_TEXT_ELEMENT_HEIGHT = 1f;
    
    public static float FLOATING_TEXT_FONT_SIZE = 48f;
    public static String FLOATING_TEXT_FONT = "consolas";
    
    
    
    private BasicGameState parent;
    
    private final Map<String,GuiElement> elements;
    
    private boolean click_lock = false;
    private int time_since_last_click = 0;
    
    
    
    public Gui () {
        elements = new HashMap<> ();
    }
    
    
    
    public Gui addElement (String name, GuiElement el) {
        elements.put(name, el);
        return this;
    }
    
    public void removeElement (String name) {
        elements.remove(name);
    }
    
    public GuiElement getElement (String name) {
        if (!elements.containsKey(name)) {
            Log.err("no element of name '"+name+"'");
            return null;
        }
        return elements.get(name);
    }
    
    public List<GuiElement> getElements (String name_contains) {
        List<GuiElement> result_list = new ArrayList<> ();
        for (String key : elements.keySet()) {
            if (key.contains(name_contains))
                result_list.add(elements.get(key));
        }
        return result_list;
    }
    
    public String addFloatingText (String text, Color col, float x, float y, float scale) {
        return addFloatingText (text, col, "", x, y, scale);
    }
    
    public String addFloatingText (String text, Color col, String icon, float x, float y, float scale) {
        long id = System.currentTimeMillis();
        
        String el_name;
        el_name = "floating_text_label_instance_"+id;
        GuiElement floating_text = new GuiElement (el_name, this, false, x, y, false, FLOATING_TEXT_ELEMENT_WIDTH, FLOATING_TEXT_ELEMENT_HEIGHT, "")
                .setText(text)
                .setFont(FLOATING_TEXT_FONT, FLOATING_TEXT_FONT_SIZE)
                .setTextColor(col.r, col.g, col.b, col.a)
                .setLayer(10)
                .setProperty("fadeout_callback", "destroy")
                .setProperty("acceleration", 0f)
                .setProperty("float_limit", 10000f)
                .setProperty("float_speed", .05f)
                .setProperty("fade_speed", 1.25f)
                .addOnUpdate("floatup")
                .instantCall("fadeout");
        if (ResourceManager.hasGraphics(icon)) floating_text.setImage(icon);
        this.addElement(el_name, floating_text);
        
        return el_name;
    }
    
    
    
    public Gui setParent (BasicGameState parent) {
        this.parent = parent;
        return this;
    }
    
    
    
    public void render (Graphics g) {
        List<GuiElement> el_list = new ArrayList<> (elements.values());
        Collections.sort(el_list, (GuiElement el1, GuiElement el2) -> el1.layer - el2.layer);
        
        for (int i=0;i<el_list.size();i++) {
            el_list.get(i).render(g,0f,0f);
        }
    }
    
    public void render (Graphics g, float x_offset, float y_offset) {
        List<GuiElement> el_list = new ArrayList<> (elements.values());
        Collections.sort(el_list, (GuiElement el1, GuiElement el2) -> el1.layer - el2.layer);
        
        for (int i=0;i<el_list.size();i++) {
            el_list.get(i).render(g, x_offset, y_offset);
        }
    }
    
    
    
    public void update (GameContainer gc, StateBasedGame sbg, int dt) {
        int mouse_x = gc.getInput().getMouseX();
        int mouse_y = gc.getInput().getMouseY();
        boolean lmb = gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON) && !click_lock;
        boolean rmb = gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON) && !click_lock;
        
        List<GuiElement> el_list = new ArrayList<> (elements.values());
        Collections.sort(el_list, (GuiElement el1, GuiElement el2) -> el2.layer - el1.layer);
        boolean clicked = false;
        for (int i=0;i<el_list.size();i++) {
            GuiElement el = el_list.get(i);
            if (el.isVisible())
                clicked = clicked || el.update(mouse_x, mouse_y, lmb && !clicked, rmb && !clicked, dt);
        }
        
        if (click_lock) {
            time_since_last_click += dt;
            if (time_since_last_click >= CLICK_LOCK_DURATION) {
                time_since_last_click = 0;
                click_lock = false;
            }
        } else if (lmb) {
            click_lock = true;
        }
    }
    
}
