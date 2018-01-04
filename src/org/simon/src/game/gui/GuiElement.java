/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.utils.Consts;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Log;
import org.simon.src.utils.Settings;

/**
 *
 * @author emil.simon
 */
public final class GuiElement {
    
    private final GuiController parent;
    private final String name;
    
    private Color color_filter;
    
    private List<Method> on_click;
    private List<Method> on_hover;
    private List<Method> on_unhover;
    private List<Method> on_update;
    private List<Method> while_hovered;
    private List<Method> while_unhovered;
    
    private String text;
    private Color text_color;
    private TrueTypeFont font;
    private Image graphics = null;
    private String graphics_name = "";
    private final Map<String,Object> properties;
    
    private Card card=null;
    private boolean is_card=false;
    
    private Creature creature=null;
    private boolean is_creature=false;
    
    private boolean visible=true;
    private boolean is_mouse_over=false;
    
    public int layer;
    public float width, height;
    public float base_width, base_height;
    public float display_x, display_y;
    public float base_x, base_y;
    public float scale;
    
    
    
    public GuiElement (String name, GuiController parent, boolean precentile_loc, float x, float y, boolean precentile_size, float width, float height, String graphics) {
        if (precentile_loc) {
            x *= Settings.screen_width;
            y *= Settings.screen_height;
        }
        if (precentile_size) {
            width *= Settings.screen_width;
            height *= Settings.screen_height;
        }
        
        this.name = name;
        this.parent = parent;
        this.properties = new HashMap<> ();
        this.color_filter = new Color (1f,1f,1f,1f);
        
        if (!graphics.isEmpty() && ResourceManager.hasGraphics(graphics)) {
            this.graphics = ResourceManager.getGraphics(graphics);
            this.graphics_name = graphics;
        }
        
        this.text = "";
        this.text_color = Color.white;
        this.font = ResourceManager.getFont(Consts.DEFAULT_FONT, Consts.DEFAULT_FONT_SIZE);
        
        this.scale = 1.0f;
        this.base_width = width;
        this.base_height = height;
        this.width = width;
        this.height = height;
        this.base_x = x;
        this.base_y = y;
        this.display_x = x;
        this.display_y = y;
        
        this.on_click = new ArrayList<> ();
        this.on_hover = new ArrayList<> ();
        this.on_unhover = new ArrayList<> ();
        this.on_update = new ArrayList<> ();
        this.while_hovered = new ArrayList<> ();
        this.while_unhovered = new ArrayList<> ();
    }
    
    public GuiElement (String name, GuiController parent, boolean precentile_loc, float x, float y, boolean precentile_size, float width, float height) {
        this (name, parent, precentile_loc, x, y, precentile_size, width, height, "");
    }
    
    
    
    public boolean isVisible () {
        return this.visible;
    }
    
    public GuiController getParent () {
        return this.parent;
    }
    
    public String getName () {
        return this.name;
    }
    
    public Image getImage () {
        return this.graphics;
    }
    
    public String getImageName () {
        return this.graphics_name;
    }
    
    
    
    public GuiElement setProperty (String key, Object value) {
        properties.put(key, value);
        return this;
    }
    
    public Object getProperty (String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        
        Log.err("Unknown property '"+key+"' of element '"+name+"'");
        return null;
    }
    
    public boolean hasProperty (String key) {
        return properties.containsKey(key);
    }
    
    public void removeProperty (String key) {
        if (hasProperty(key))
            properties.remove(key);
    }
    
    
    
    public GuiElement setCard (Card card) {
        this.card = card;
        this.is_card = true;
        
        return this;
    }
    
    public Card getCard () {
        return card;
    }
    
    public GuiElement setCreature (final Creature creature) {
        this.creature = creature;
        this.is_creature = true;
        this.creature.setGuiElement(this);
        
        return this;
    }
    
    public Creature getCreature () {
        return creature;
    }
    
    public GuiElement setText (String text) {
        this.text = text;
        return this;
    }
    
    public GuiElement setSize (float width, float height) {
        this.base_width = width;
        this.base_height = height;
        this.width = width;
        this.height = height;
        return this;
    }
    
    public GuiElement setColor (float r, float g, float b, float a) {
        this.color_filter = new Color (r,g,b,a);
        return this;
    }
    
    public GuiElement setColor (Color col) {
        this.color_filter = col;
        return this;
    }
    
    public Color getColor () {
        return this.color_filter;
    }
    
    public Color getTextColor () {
        return this.text_color;
    }
    
    public GuiElement setTextColor (float r, float g, float b, float a) {
        this.text_color = new Color (r,g,b,a);
        return this;
    }
    
    public GuiElement setFont (String font_name, int size) {
        this.font = ResourceManager.getFont(font_name, size);
        return this;
    }
    
    public GuiElement setLayer (int layer) {
        this.layer = layer;
        return this;
    }
    
    public GuiElement setVisible (boolean visible) {
        this.visible = visible;
        return this;
    }
    
    public GuiElement setImage (String img) {
        if (ResourceManager.hasGraphics(img)) {
            this.graphics = ResourceManager.getGraphics(img);
            this.graphics_name = img;
        }
        return this;
    }
    
    public float getCenterX () {
        float c_x = this.display_x + width/2f;
        return c_x;
    }
    
    public float getCenterY () {
        float c_x = this.display_y + height/2f;
        return c_x;
    }
    
    
    
    public GuiElement setOnClick (String on_click_str) {
        if (on_click_str.isEmpty()) {
            this.on_click = new ArrayList<> ();
            return this;
        }
        
        String[] on_clicks = on_click_str.split("&");
        
        for (String on_click : on_clicks) {
            try {
                if (!on_click.isEmpty()) {
                    this.on_click.add(GuiActionHandler.class.getDeclaredMethod(on_click, this.getClass()));
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnHover (String on_hover_str) {
        String[] on_hovers = on_hover_str.split("&");
        
        for (String on_hover : on_hovers) {
            try {
                if (!on_hover.isEmpty()) this.on_hover.add(GuiActionHandler.class.getDeclaredMethod(on_hover, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnUnhover (String on_unhover_str) {
        String[] on_unhovers = on_unhover_str.split("&");
        
        for (String on_unhover : on_unhovers) {
            try {
                if (!on_unhover.isEmpty()) this.on_unhover.add(GuiActionHandler.class.getDeclaredMethod(on_unhover, this.getClass()));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setWhileHovered (String while_hover_str) {
        String[] while_hovers = while_hover_str.split("&");
        
        for (String while_hover : while_hovers) {
            try {
                if (!while_hover.isEmpty()) this.while_hovered.add(GuiActionHandler.class.getDeclaredMethod(while_hover, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setWhileUnhovered (String while_unhover_str) {
        String[] while_unhovers = while_unhover_str.split("&");
        
        for (String while_unhover : while_unhovers) {
            try {
                if (!while_unhover.isEmpty()) this.while_unhovered.add(GuiActionHandler.class.getDeclaredMethod(while_unhover, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement setOnUpdate (String on_update_str) {
        String[] on_updates = on_update_str.split("&");
        this.on_update = new ArrayList<> ();
        
        for (String on_update : on_updates) {
            try {
                if (!on_update.isEmpty()) this.on_update.add(GuiActionHandler.class.getDeclaredMethod(on_update, this.getClass(), float.class));
            } catch (NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    public GuiElement addOnUpdate (String name) {
        try {
            if (!name.isEmpty()) this.on_update.add(GuiActionHandler.class.getDeclaredMethod(name, this.getClass(), float.class));
        } catch (NoSuchMethodException | SecurityException ex) {
            Log.err(ex);
        }
        return this;
    }
    
    public void removeOnUpdate (String name) {
        for (int i=0;i<on_update.size();i++) {
            if (on_update.get(i).getName().equals(name))
                on_update.remove(i);
        }
    }
    
    public boolean hasOnUpdate (String name) {
        for (int i=0;i<on_update.size();i++) {
            Method update = on_update.get(i);
            if (name.equals(update.getName()))
                return true;
        }
        return false;
    }
    
    
    
    private void onClick () {
        try {
            if (this.on_click != null)
                for (int i=0;i<this.on_click.size();i++)
                    this.on_click.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onHover () {
        try {
            if (this.on_hover != null)
                for (int i=0;i<this.on_hover.size();i++)
                    this.on_hover.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onUnhover () {
        try {
            if (this.on_unhover != null)
                for (int i=0;i<this.on_unhover.size();i++)
                    this.on_unhover.get(i).invoke(null, this);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void onUpdate (int dt) {
        try {
            if (this.on_update != null)
                for (int i=0;i<this.on_update.size();i++)
                    this.on_update.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void whileHovered (int dt) {
        try {
            if (this.while_hovered != null)
                for (int i=0;i<this.while_hovered.size();i++)
                    this.while_hovered.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    private void whileUnhovered (int dt) {
        try {
            if (this.while_unhovered != null)
                for (int i=0;i<this.while_unhovered.size();i++)
                    this.while_unhovered.get(i).invoke(null, this, dt);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err(ex);
        }
    }
    
    public GuiElement instantCall (String callstring) {
        String[] instant_calls = callstring.split("&");
        
        for (String instant_call : instant_calls) {
            try {
                if (!instant_call.isEmpty()) {
                    Method method = GuiActionHandler.class.getDeclaredMethod(instant_call, this.getClass());
                    method.invoke(null, this);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                Log.err(ex);
            }
        }
        return this;
    }
    
    
    
    public void render (Graphics g, float x_offset, float y_offset) {
        if (!this.visible) return;
        
        if (Settings.debug_gui && Settings.debug_mode) {
            g.setColor(color_filter);
            g.fillRect(display_x+x_offset, display_y+y_offset, width, height);
        }
        
        if (graphics!=null)
            graphics.draw(display_x+x_offset, display_y+y_offset, width, height, color_filter);
        
        if (!text.isEmpty()) {
            // TEXT DOESN'T SCALE !!! - it uses font size
            
            String[] lines = text.split("\n");
            int entire_width = 0;
            int entire_height = 0;
            
            for (String line : lines) {
                entire_width = Math.max(font.getWidth(line), entire_width);
                entire_height += font.getHeight(line);
            }
            
            int top_x = (int)(display_x+x_offset + width/2f - entire_width/2f);
            int top_y = (int)(display_y+y_offset + height/2f - entire_height/2f);
            
            g.setFont(font);
            g.setColor(text_color);
            int text_x, text_y = top_y;
            for (int i=0;i<lines.length;i++) {
                String line = lines[i];
                text_x = (int)(top_x + entire_width/2f - font.getWidth(line)/2f);
                g.drawString(line, text_x, text_y);
                text_y += font.getHeight(line);
            }
        }
        
        if (this.is_card && card!=null) {
            float actual_scale = scale * (base_width / Card.STANDARD_CARD_WIDTH);
            this.card.render(g, display_x+x_offset, display_y+y_offset, actual_scale);
        }
        
        if (this.is_creature && creature!=null) {
            this.creature.render(g, display_x+x_offset + width/2f, display_y+y_offset + height/2f, width, scale);
        }
    }
    
    
    
    public boolean update (int mouse_x, int mouse_y, boolean lmb, boolean rmb, int dt) {
        boolean was_clicked = false;
        
        if (is_mouse_over) {
            this.whileHovered(dt);
        } else {
            this.whileUnhovered(dt);
        }
        
        if (on_update!=null && !on_update.isEmpty()) {
            this.onUpdate(dt);
        }
        
        if (mouse_x < display_x+width && mouse_x > display_x && mouse_y < display_y+height && mouse_y > display_y) {
            if (lmb) {
                this.onClick();
                was_clicked = true;
            }
            
            if (!is_mouse_over) {
                this.onHover();
            }
            
            is_mouse_over = true;
        } else {
            if (is_mouse_over) {
                this.onUnhover();
            }
            
            is_mouse_over = false;
        }
        
        return was_clicked;
    }
    
}
