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
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.save.SavedStateFactory;
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
    
    public static final float FLOATING_TEXT_X_OFFSET = 48f;
    public static final float FLOATING_TEXT_Y_OFFSET = 32f;
    
    public static final int POINTS_RENDERED_FOR_WIDTH = 10;
    public static final float POINT_RENDER_MARGIN = 0.05f;
    public static final float TURN_INDICATOR_MARGIN = 0.1f;

    public static final float DEFAULT_CREATURE_WIDTH = 0.13f;
    public static final float DEFAULT_CREATURE_HEIGHT = 0.3f;
    public static final float MAX_SCALING = 4f;
    
    public static final int STAT_FONT_SIZE = 14;
    public static final float STAT_ICON_MARGIN = 0.25f;
    public static final String STAT_FONT_NAME = "consolas";
    public static final float STAT_ICON_SIZE_FROM_WIDTH = 0.1f;
    
    public static final Image STAT_ICON_ARMOR = ResourceManager.getGraphics("stats/armor");
    public static final Image STAT_ICON_HEALTH = ResourceManager.getGraphics("stats/health");
    public static final Image STAT_ICON_ATKMOD = ResourceManager.getGraphics("stats/atkmod");
    public static final TrueTypeFont STAT_FONT = ResourceManager.getFont(STAT_FONT_NAME, STAT_FONT_SIZE);
    
    private String id;
    private String name;
    private String creature_pack;
    
    private Image graphics;
    private float graphics_scale;
    private String graphics_name;
    private Creature parent = null;
    
    private int disabled, armor, atk_mod, base_atk_mod;
    private int health_current, health_max;
    private float difficulty;
    
    private final List<Card> card_list;
    
    private boolean is_player_character = false;
    private final List<StatusEffect> status_effects;
    
    private final Map<PointTypeEnum, Integer> used_point_pool;
    private final Map<PointTypeEnum, Integer> total_point_pool;
    
    private GuiElement gui_element = null;
    
    
    
    public Creature () {
        this("","","","",1f,1f,10,0,0);
    }
    
    public Creature (final Creature parent) {
        disabled = 0;
        
        this.parent = parent;
        
        this.id = parent.id;
        this.name = parent.name;
        this.graphics_name = parent.graphics_name;
        this.graphics_scale = parent.graphics_scale;
        this.armor = parent.armor;
        this.atk_mod = parent.atk_mod;
        this.base_atk_mod = parent.base_atk_mod;
        this.health_max = parent.health_max;
        this.health_current = parent.health_current;
        this.difficulty = parent.difficulty;
        
        if (ResourceManager.hasGraphics(graphics_name)) {
            this.graphics = ResourceManager.getGraphics(graphics_name);
        }
        
        this.card_list = new ArrayList<> (parent.card_list);
        
        this.status_effects = new ArrayList<> ();
        this.used_point_pool = new HashMap<> ();
        this.total_point_pool = new HashMap<> ();
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum point_type = PointTypeEnum.values()[i];
            used_point_pool.put(point_type, 0);
            total_point_pool.put( point_type, parent.getTotalPoints(point_type) );
        }
    }
    
    public Creature (String id, String creature_pack, String name, String graphics, float graphics_scale, float difficulty, int hp, int armor, int atk_mod) {
        disabled = 0;
        
        this.id = id;
        this.name = name;
        this.creature_pack = creature_pack;
        
        this.armor = armor;
        this.atk_mod = atk_mod;
        this.base_atk_mod = atk_mod;
        this.health_max = hp;
        this.health_current = hp;
        this.difficulty = difficulty;
        
        this.graphics_scale = graphics_scale;
        if (ResourceManager.hasGraphics(graphics)) {
            this.graphics = ResourceManager.getGraphics(graphics);
            this.graphics_name = graphics;
        }
        
        this.card_list = new ArrayList<> ();
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
    
    public String getGraphicsName () {
        return graphics_name;
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
    
    public float getCurrentHealthPercent () {
        return health_current / health_max;
    }
    
    public int getTotalPoints (PointTypeEnum type) {
        return this.total_point_pool.get(type);
    }
    
    public int getTotalPoints () {
        int result = 0;
        for (PointTypeEnum type : PointTypeEnum.values()) {
            result += getTotalPoints(type);
        }
        return result;
    }
    
    public float getGraphicsScale () {
        return graphics_scale;
    }
    
    public float getDifficulty () {
        return difficulty;
    }
    
    public GuiElement getGuiElement () {
        return gui_element;
    }
    
    public Creature getParent () {
        return parent;
    }
    
    public List<StatusEffect> getStatusEffects () {
        return status_effects;
    }
    
    public Map<PointTypeEnum, Integer> getUsedPointPool () {
        return used_point_pool;
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
    
    public void setGraphics (String graphics) {
        if (!ResourceManager.hasGraphics(graphics)) {
            Log.err("Cannot set creature '"+name+"' graphics to '"+graphics+"' because none such graphic was loaded!");
            return;
        }
        
        this.graphics_name = graphics;
        this.graphics = ResourceManager.getGraphics(graphics);
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
        
        if (this.parent==null && element.getParent()==CombatState.gui) Log.err("WARNING: bound creature type definition (creature with no parent) '"+id+"' to gui element '"+element.getName()+"'!");
    }
    
    
    
    public void setMaxHealth (int health) {
        float ratio = health_current / health_max;
        health_max = health;
        health_current = (int) (health*ratio);
    }
    
    public void setArmor (int armor) {
        this.armor = armor;
    }
    
    public void setCurrentHealth (int current_health) {
        this.health_current = current_health;
    }
    
    public void setUsedPointPool (HashMap<PointTypeEnum, Integer> used_point_pool) {
        for (PointTypeEnum type : PointTypeEnum.values()) {
            this.used_point_pool.put(type, used_point_pool.get(type));
        }
    }
    
    public void setIsPlayerCharacter (boolean is_player_character) {
        this.is_player_character = is_player_character;
    }
    
    public void setDifficulty (float difficulty) {
        this.difficulty = difficulty;
    }
    
    public void addCard (String card_name) {
        Card card_to_add = CardLibrary.getCard(card_name);
        if (card_to_add!=null) {
            this.card_list.add(card_to_add);
        } else Log.err("Cannot add card '"+card_name+"' to creature '"+id+"' because no such card was loaded!");
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
        if (isDead()) return false;
        
        Map<PointTypeEnum, Integer> requested_pool = card.getPointPool();
        for (PointTypeEnum type : PointTypeEnum.values()) {
            int unused = total_point_pool.get(type) - used_point_pool.get(type);
            if (requested_pool.get(type) > unused) {
                return false;
            }
        }
        return true;
    }
    
    public boolean canSpendPoints (Map<PointTypeEnum, Integer> point_pool, Card card) {
        if (isDead()) return false;
        
        Map<PointTypeEnum, Integer> requested_pool = card.getPointPool();
        for (PointTypeEnum type : PointTypeEnum.values()) {
            int unused = total_point_pool.get(type) - point_pool.get(type);
            if (requested_pool.get(type) > unused) {
                return false;
            }
        }
        return true;
    }
    
    public List<Card> getCastableCards () {
        List<Card> result = new ArrayList<> ();
        if (!isDead()) {
            for (Card card : card_list) {
                if (canSpendPoints(card)) result.add(card);
            }
        }
        return result;
    }
    
    public List<Card> getCastableCards (Map<PointTypeEnum, Integer> point_pool) {
        List<Card> result = new ArrayList<> ();
        if (!isDead()) {
            for (Card card : card_list) {
                if (canSpendPoints(point_pool, card)) result.add(card);
            }
        }
        return result;
    }
    
    
    
    public void addFloatingText (String text, Color color) {
        this.gui_element.getParent().addFloatingText(text, color, gui_element.getCenterX()+FLOATING_TEXT_X_OFFSET, gui_element.getCenterY()+FLOATING_TEXT_Y_OFFSET);
    }
    
    
    
    public void reduceHealth (int amount, Creature source) {
        int real_amount = amount;
        if (this.armor > 0) {
            real_amount = Math.max(0, amount-armor);
            armor = Math.max(0, armor-amount);
        }
        this.health_current -= real_amount;
        if (isDead()) die();
    }
    
    private void die () {
        if (gui_element==null) return;
        gui_element.instantCall("fadeout");
        Log.log("Creature '"+id+"' at '"+gui_element.getName()+"' has died!");
        
        if (GameplayManager.isCreatureEnemy(this)) {
            // im an enemy! :-D
            Player.addScore((int) this.difficulty);
            
            if (GameplayManager.allEnemiesDead()) {
                if (GameplayManager.isLevelOver()) {
                    GameplayManager.setTotalDifficulty(0);
                    CombatState.generateNextLvlGui();
                    CombatState.next_level = true;
                    CombatState.next_lvl_gui.callForElements("", "fadein");
                } else {
                    // ... and all my friends are dead :,(
                    CombatState.gui.addFloatingText(GameplayManager.WAVE_CLEARED_TEXT, GameplayManager.WAVE_CLEARED_COLOR, Settings.screen_width/2f, Settings.screen_height/2f);
                    Log.log(GameplayManager.WAVE_CLEARED_TEXT + " !");
                    // game progress is automatically saved whenever a wave is cleared
                    SavedStateFactory.save();
                }
            }
        } else if (!GameplayManager.isCreatureEnemy(this) && GameplayManager.allAlliesDead()) {
            // game over brah   
            CombatState.gameover();
            Log.log(GameplayManager.GAMEOVER_TEXT + " !");
        }
    }
    
    public void restoreHealth (int amount, Creature source) {
        this.health_current = Math.min(this.health_current + amount, this.health_max);
    }
    
    public void restorePoints () {
        for (PointTypeEnum type : PointTypeEnum.values())
            restorePoints(type);
    }
    
    public void restorePoints (PointTypeEnum for_type) {
        used_point_pool.put(for_type, 0);
    }
    
    public void disable (int turns) {
        this.disabled = Math.max(turns, this.disabled);
    }
    
    public void addArmor (int amount) {
        this.armor += amount;
    }
    
    public void addAttackMod (int amount) {
        this.atk_mod += amount;
    }
    
    public void consumeAttackMod () {
        this.atk_mod = 0;
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
    
    public boolean isDisabled () {
        return disabled>0;
    }
    
    public boolean isDead () {
        return health_current<=0;
    }
    
    public boolean isPlayerCharacter () {
        return is_player_character;
    }
    
    public void turnTick (SpecialEffectSystem sfx) {
        if (isDead()) return;
        
        for (int i=0;i<status_effects.size();i++) {
            status_effects.get(i).turnTick(sfx);
            if (status_effects.get(i).isDone()) status_effects.remove(i);
        }
        if (disabled>0) disabled--;
    }
    
    
    
    public void render (Graphics g, float x, float y, float target_width, float target_height, float scale) {
        // creatures without a display image are not rendered
        if (this.graphics == null) {
            return;
        }
        
        // determine actual rendering x,y and dimensions (scaling)
        float width_scale_factor = Math.min((target_width/graphics.getWidth()) * scale * graphics_scale, MAX_SCALING);
        float actual_width = graphics.getWidth() * width_scale_factor;
        float actual_height = graphics.getHeight() * width_scale_factor;

        float actual_x = x - actual_width/2f;
        float actual_y = y - actual_height/2f;
        
        Color gui_color = gui_element.getColor();
        Color stat_filter = new Color (1f,1f,1f,gui_color.a);
        
        if (isDisabled()) {
            gui_color = new Color (gui_color.r / 2f, gui_color.g / 2f, gui_color.b / 2f, gui_color.a);
        }

        // render creature image at actual x,y with actual dimensions
        graphics.draw(actual_x, actual_y, width_scale_factor, gui_color);
        
        // render base stats (cur hp, armor, base_atk_mod) at lower left corner of allocated, going up
        float stats_icon_size = target_width * STAT_ICON_SIZE_FROM_WIDTH;
        float stats_x = x - (target_width/2f);
        float stats_y = y + Math.min(actual_height/2f, target_height/2f);
        float text_x = stats_x + stats_icon_size * (1f+STAT_ICON_MARGIN);
        
        g.setColor(stat_filter);
        g.setFont(STAT_FONT);
        
        STAT_ICON_HEALTH.draw(stats_x, stats_y, stats_icon_size, stats_icon_size, stat_filter);
        g.drawString(Math.max(this.health_current,0)+"/"+this.health_max, text_x, stats_y);
        
        if (armor>0) {
            stats_y -= stats_icon_size; // increment to add margins between stats
            STAT_ICON_ARMOR.draw(stats_x, stats_y, stats_icon_size, stats_icon_size, stat_filter);
            g.drawString(String.valueOf(this.armor), text_x, stats_y);
        }
        
        if (atk_mod!=0) {
            stats_y -= stats_icon_size; // increment to add margins between stats
            STAT_ICON_ATKMOD.draw(stats_x, stats_y, stats_icon_size, stats_icon_size, stat_filter);
            g.drawString(String.valueOf(this.atk_mod), text_x, stats_y);
        }
        
        // render currently active statuses (poison, burn, atk_mod, disabled) at lower right corner of allocated, going up
        float status_icon_size = stats_icon_size;
        float status_x = x + (target_width/2f) - status_icon_size;
        float status_y = y + Math.min(actual_height/2f, target_height/2f);
        
        for (int i=0;i<status_effects.size();i++) {
            Image icon = ResourceManager.getGraphics(status_effects.get(i).getDisplayIcon());
            if (icon==null) continue;
            
            icon.draw(status_x, status_y, status_icon_size, status_icon_size, stat_filter);
            status_y -= status_icon_size;
        }
        
        
        // render total points at top - render used points darker
        int point_sum = 0;
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            point_sum += this.total_point_pool.get(type);
        }
        
        float point_margin = (target_width / point_sum) * POINT_RENDER_MARGIN;
        float point_size = (target_width / point_sum) - point_margin;
        float point_max_size = (target_width / POINTS_RENDERED_FOR_WIDTH) - point_margin;
        point_size = Math.min (point_size,point_max_size);
        float point_y_offset = -point_size;
        float point_x_offset = -( (point_size+point_margin) * point_sum ) / 2f;
        
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            Integer points = this.total_point_pool.get(type);
            Integer used = this.used_point_pool.get(type);
            Integer unused = points - used;
            float point_y = Math.max((y - (actual_height/2f)), (y - (target_height/2f))) + point_y_offset;
            
            for (int j=0;j<unused;j++) {
                type.render(g, x + point_x_offset, point_y, point_size, point_size, gui_color.a);
                point_x_offset += point_margin + point_size;
            }
            
            for (int j=unused;j<points;j++) {
                type.renderUsed(g, x + point_x_offset, point_y, point_size, point_size, gui_color.a);
                point_x_offset += point_margin + point_size;
            }
        }
        
        // if this creature is the one currently on turn, render turn indicator
        if (CombatState.getCurrentCastingCreature() == this) {
            Image turn_indicator = ResourceManager.getGraphics(Settings.turn_indicator_graphics);
            float turn_indicator_width = target_width /3f;
            float turn_indicator_height = turn_indicator_width;
            turn_indicator.draw(x - turn_indicator_width/2f, y - actual_height/2 - turn_indicator_height*(1f + TURN_INDICATOR_MARGIN) - point_size, turn_indicator_width, turn_indicator_height);
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
        
        String graphics_size_string = "";
        graphics_size_string = String.format("%.2f",this.graphics_scale);
        
        String contents = "";
        contents += "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_NEW_CREATURE + CreatureLibrary.PARSE_DELIMITER + " " + this.id + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_NAME + CreatureLibrary.PARSE_DELIMITER + " " + this.name + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_GRAPHICS + CreatureLibrary.PARSE_DELIMITER + " " + this.graphics_name + "\n";
        contents += CreatureLibrary.PARSE_KEYWORD_SET_GRAPHICS_SCALE + CreatureLibrary.PARSE_DELIMITER + " " + graphics_size_string + "\n";
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
