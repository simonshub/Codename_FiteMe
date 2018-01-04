/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.creatures;

import org.simon.src.game.data.gameplay.StatusEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.Settings;

/**
 *
 * @author XyRoN
 */
public class Creature {
    
    public static final int POINTS_RENDERED_FOR_WIDTH = 10;
    public static final float POINT_RENDER_MARGIN = 0.05f;
    public static final float TURN_INDICATOR_MARGIN = 0.1f;

    public static final float DEFAULT_CREATURE_WIDTH = 0.13f;
    public static final float DEFAULT_CREATURE_HEIGHT = 0.3f;
    public static final float MAX_SCALING = 2f;
    
    public static final int STAT_FONT_SIZE = 14;
    public static final float STAT_ICON_MARGIN = 0.5f;
    public static final String STAT_FONT_NAME = "consolas";
    public static final float STAT_ICON_SIZE_FROM_WIDTH = 0.1f;
    
    public static final Image STAT_ICON_ARMOR = ResourceManager.getGraphics("stats/armor");
    public static final Image STAT_ICON_HEALTH = ResourceManager.getGraphics("stats/health");
    public static final TrueTypeFont STAT_FONT = ResourceManager.getFont(STAT_FONT_NAME, STAT_FONT_SIZE);
    
    private String id;
    private String name;
    private String creature_pack;
    
    private Image icon;
    private float icon_scale;
    private String icon_name;
    private Creature parent = null;
    
    private int disabled, armor, atk_mod, base_atk_mod;
    private int health_current, health_max;
    
    private boolean dead;
    private boolean is_player_character = false;
    
    private final List<StatusEffect> status_effects;
    
    private final Map<PointTypeEnum, Integer> used_point_pool;
    private final Map<PointTypeEnum, Integer> total_point_pool;
    
    private GuiElement gui_element = null;
    
    
    
    public Creature () {
        this("","","","",1f,10,0,0);
    }
    
    public Creature (final Creature parent) {
        disabled = 0;
        dead = false;
        
        this.parent = parent;
        
        this.id = parent.getId();
        this.name = parent.getName();
        this.icon_name = parent.getIcon();
        this.icon_scale = parent.getIconScale();
        this.armor = parent.armor;
        this.atk_mod = parent.atk_mod;
        this.base_atk_mod = parent.base_atk_mod;
        this.health_max = parent.health_max;
        this.health_current = parent.health_current;
        
        if (ResourceManager.hasGraphics(icon_name)) {
            this.icon = ResourceManager.getGraphics(icon_name);
        }
        
        this.status_effects = new ArrayList<> ();
        this.used_point_pool = new HashMap<> ();
        this.total_point_pool = new HashMap<> ();
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum point_type = PointTypeEnum.values()[i];
            used_point_pool.put(point_type, 0);
            total_point_pool.put( point_type, parent.getTotalPoints(point_type) );
        }
    }
    
    public Creature (String id, String creature_pack, String name, String icon, float icon_scale, int hp, int armor, int atk_mod) {
        disabled = 0;
        dead = false;
        
        this.id = id;
        this.name = name;
        this.creature_pack = creature_pack;
        
        this.armor = armor;
        this.atk_mod = atk_mod;
        this.base_atk_mod = atk_mod;
        this.health_max = hp;
        this.health_current = hp;
        
        this.icon_scale = icon_scale;
        if (ResourceManager.hasGraphics(icon)) {
            this.icon = ResourceManager.getGraphics(icon);
            this.icon_name = icon;
        }
        
        this.status_effects = new ArrayList<> ();
        this.used_point_pool = new HashMap<> ();
        this.total_point_pool = new HashMap<> ();
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum point_type = PointTypeEnum.values()[i];
            used_point_pool.put(point_type, 0);
            total_point_pool.put(point_type, 0);
        }
    }
    
    
    
    public String getName () {
        return name;
    }
    
    public String getIcon () {
        return icon_name;
    }
    
    public String getId () {
        return id;
    }
    
    public String getCreaturePack () {
        return creature_pack;
    }
    
    public int getCreaturePackIndex () {
        String[] all_packs = CreatureLibrary.getLoadedCreaturePacks();
        for (int i=0;i<all_packs.length;i++)
            if (all_packs[i].equals(this.creature_pack))
                return i;
        return -1;
    }
    
    public int getArmor () {
        return armor;
    }
    
    public int getAttackModifier () {
        return atk_mod;
    }
    
    public int getBaseAttackModifier () {
        return base_atk_mod;
    }
    
    public int getMaxHealth () {
        return health_max;
    }
    
    public int getCurrentHealth () {
        return health_current;
    }
    
    public int getTotalPoints (PointTypeEnum type) {
        return this.total_point_pool.get(type);
    }
    
    public float getIconScale () {
        return icon_scale;
    }
    
    public GuiElement getGuiElement () {
        return gui_element;
    }
    
    
    
    public void assignIdOverwrite (String new_id) {
        this.id = new_id;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public void setId (String id) {
        this.id = id;
    }
    
    public void setIcon (String icon) {
        if (!ResourceManager.hasGraphics(icon)) {
            Log.err("Cannot set creature '"+name+"' graphics to '"+icon+"' because none such graphic was loaded!");
            return;
        }
        
        this.icon_name = icon;
        this.icon = ResourceManager.getGraphics(icon);
    }
    
    public void setParent (Creature parent) {
        this.parent = parent;
    }
    
    public void setPoints (HashMap<PointTypeEnum, Integer> point_map) {
        for (PointTypeEnum type : PointTypeEnum.values()) {
            if (!point_map.containsKey(type)) {
                this.used_point_pool.put(type, 0);
                this.total_point_pool.put(type, 0);
            } else {
                this.used_point_pool.put(type, 0);
                this.total_point_pool.put(type, point_map.get(type));
            }
        }
    }
    
    public void setGuiElement (GuiElement element) {
        this.gui_element = element;
    }
    
    public void setHealth (int health) {
        float ratio = health_current / health_max;
        health_max = health;
        health_current = (int) (health*ratio);
    }
    
    public void setIsPlayerCharacter (boolean is_player_character) {
        this.is_player_character = is_player_character;
    }
    
    
    
    public boolean spendPoints (Card card) {
        Map<PointTypeEnum, Integer> requested_pool = card.getPointPool();
        Map<PointTypeEnum, Integer> tmp_used_pool = new HashMap<> ();
        for (PointTypeEnum type : PointTypeEnum.values())
            if (used_point_pool.containsKey(type))
                tmp_used_pool.put(type, used_point_pool.get(type));
        
        for (PointTypeEnum type : PointTypeEnum.values()) {
            if (requested_pool.containsKey(type) && total_point_pool.containsKey(type)) {
                Integer unused = total_point_pool.get(type) - tmp_used_pool.get(type);
                
                Integer used = requested_pool.get(type);
                if (tmp_used_pool.containsKey(type))
                    used += tmp_used_pool.get(type);
                
                if (unused >= requested_pool.get(type))
                    tmp_used_pool.put(type, used);
                else return false;
            }
        }
        
        for (PointTypeEnum type : PointTypeEnum.values())
            used_point_pool.put(type, tmp_used_pool.get(type));
        
        return true;
    }
    
    public boolean canSpendPoints (Card card) {
        Map<PointTypeEnum, Integer> requested_pool = card.getPointPool();
        for (PointTypeEnum type : PointTypeEnum.values()) {
            int unused = total_point_pool.get(type) - used_point_pool.get(type);
            if (requested_pool.get(type) > unused) {
                return false;
            }
        }
        return true;
    }
    
    public void addFloatingText (String text, Color color) {
        this.gui_element.getParent().addFloatingText(text, color, gui_element.getCenterX(), gui_element.getCenterY(), gui_element.scale);
    }
    
    
    
    public void reduceHealth (int amount, Creature source) {
        int real_amount = amount;
        if (this.armor > 0) {
            real_amount = Math.max(0, amount-armor);
            armor = Math.max(0, armor-amount);
        }
        this.health_current -= real_amount;
        
        if (this.health_current <= 0) {
            dead = true;
        }
    }
    
    public void restoreHealth (int amount, Creature source) {
        this.health_current = Math.min(this.health_current + amount, this.health_max);
    }
    
    public void disable (int turns) {
        this.disabled = Math.max(turns, this.disabled);
    }
    
    public void addArmor (int amount) {
        this.armor += amount;
    }
    
    public void removeArmor (int amount) {
        this.armor = Math.max(0, armor-amount);
    }
    
    public void addStatusEffect (StatusEffect status_effect) {
        this.status_effects.add(status_effect);
    }
    
    public boolean removeStatusEffect (StatusEffect status_effect) {
        return this.status_effects.remove(status_effect);
    }
    
    public void turnTick (SpecialEffectSystem sfx) {
        for (int i=0;i<status_effects.size();i++) {
            status_effects.get(i).turnTick(sfx);
        }
    }
    
    
    
    public void render (Graphics g, float x, float y, float target_width, float scale) {
        // creatures without a display image are not rendered
        if (this.icon == null) {
            return;
        }
        
        // determine actual rendering x,y and dimensions (scaling)
        float width_scale_factor = Math.min( (target_width/icon.getWidth()) * scale * icon_scale, MAX_SCALING);
        float actual_width = icon.getWidth() * width_scale_factor;
        float actual_height = icon.getHeight() * width_scale_factor;

        float actual_x = x - actual_width/2f;
        float actual_y = y - actual_height/2f;

        // render creature image at actual x,y with actual dimensions
        icon.draw(actual_x, actual_y, width_scale_factor);
        
        // render base stats (cur hp, armor, base_atk_mod) at lower left corner of allocated, going up
        int n_of_stats_displayed = 1;
        if (armor>0) n_of_stats_displayed++;
        
        float stats_icon_size = target_width * STAT_ICON_SIZE_FROM_WIDTH;
        float stats_x = x - (target_width/2f);
        float stats_y = actual_y + actual_height*1.1f + ( (n_of_stats_displayed-1) * stats_icon_size * 1.25f); // height is amplified by 10% as a margin
        float text_x = stats_x + stats_icon_size * (1f+STAT_ICON_MARGIN);
        
        g.setColor(Color.white);
        g.setFont(STAT_FONT);
        
        STAT_ICON_HEALTH.draw(stats_x, stats_y, stats_icon_size, stats_icon_size);
        g.drawString(this.health_current+"/"+this.health_max, text_x, stats_y);
        
        if (armor>0) {
            stats_y -= stats_icon_size * 1.25f; // increment to add margins between stats
            STAT_ICON_ARMOR.draw(stats_x, stats_y, stats_icon_size, stats_icon_size);
            g.drawString(String.valueOf(this.armor), text_x, stats_y);
        }
        
        // render currently active statuses (poison, burn, atk_mod, disabled) at lower right corner of allocated, going up
        
        // render total points at upper left corner, going right - render used points darker
        float point_x_offset = 0;
        float point_y_offset = 0;
        
        int point_sum = 0;
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            point_sum += this.total_point_pool.get(type);
        }
        
        float point_margin = (target_width / point_sum) * POINT_RENDER_MARGIN;
        float point_size = (target_width / point_sum) - point_margin;
        float point_max_size = (target_width / POINTS_RENDERED_FOR_WIDTH) - point_margin;
        point_size = Math.min (point_size,point_max_size);
        point_y_offset -= point_size;
        point_x_offset -= ( (point_size+point_margin) * point_sum ) / 2f;
        
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            Integer points = this.total_point_pool.get(type);
            Integer used = this.used_point_pool.get(type);
            Integer unused = points - used;
            
            for (int j=0;j<unused;j++) {
                type.render(g, x+point_x_offset, actual_y+point_y_offset, point_size, point_size);
                point_x_offset += point_margin + point_size;
            }
            
            for (int j=unused;j<points;j++) {
                type.renderUsed(g, x+point_x_offset, actual_y+point_y_offset, point_size, point_size);
                point_x_offset += point_margin + point_size;
            }
        }
        
        // if this creature is the one currently on turn, render turn indicator
        if (CombatState.getCurrentCastingCreature() == this) {
            Image turn_indicator = ResourceManager.getGraphics(Settings.turn_indicator_graphics);
            float turn_indicator_width = actual_width /2f;
            float turn_indicator_height = actual_width /2f;
            turn_indicator.draw(actual_x + actual_width/2f - turn_indicator_width/2f, actual_y - turn_indicator_height*(1f + TURN_INDICATOR_MARGIN) - point_size, turn_indicator_width, turn_indicator_height);
        }
    }
    
    
    
    @Override
    public String toString () {
        String point_string = "";
        for (PointTypeEnum type : PointTypeEnum.values()) {
            if (total_point_pool.get(type) > 0)
                point_string += type.code + " " + total_point_pool.get(type).toString() + " ";
        }
        point_string = point_string.trim();
        
        String icon_size_string = "";
        icon_size_string = String.format("%.2f",this.icon_scale);
        
        String contents = "";
        contents += "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_NEW_CREATURE + CreatureLibrary.PARSE_DELIMITER + " " + this.id + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_NAME + CreatureLibrary.PARSE_DELIMITER + " " + this.name + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_ICON + CreatureLibrary.PARSE_DELIMITER + " " + this.icon_name + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_ICON_SCALE + CreatureLibrary.PARSE_DELIMITER + " " + icon_size_string + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_POINTS + CreatureLibrary.PARSE_DELIMITER + " " + point_string + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_HEALTH + CreatureLibrary.PARSE_DELIMITER + " " + this.health_max + "\n";
        if (this.armor > 0)
            contents += CreatureLibrary.PARSE_KEYWORD_SET_ARMOR + CreatureLibrary.PARSE_DELIMITER + " " + this.armor + "\n";
        if (this.atk_mod > 0)
            contents += CreatureLibrary.PARSE_KEYWORD_SET_ATK_MOD + CreatureLibrary.PARSE_DELIMITER + " " + this.atk_mod + "\n";
        contents += "\n";
        
        return contents;
    }
    
}
