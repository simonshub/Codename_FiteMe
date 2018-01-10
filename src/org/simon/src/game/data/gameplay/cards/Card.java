/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

import org.simon.src.game.data.gameplay.TargetEnum;
import org.simon.src.game.data.gameplay.creatures.Creature;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.TrueTypeFont;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.sfx.SpecialEffectCallback;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.states.SharedState;
import org.simon.src.game.states.combat.CombatState;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;

/**
 *
 * @author XyRoN
 */
public class Card {
    
    public static final float STANDARD_CARD_WIDTH = 100f;           // in pixels
    public static final float STANDARD_CARD_HEIGHT = 150f;          // in pixels
    public static final float STANDARD_SHADOW_WIDTH = 200f;         // in pixels
    public static final float STANDARD_SHADOW_HEIGHT = 300f;        // in pixels
    public static final float CARD_X_MARGIN = 5f;                   // in pixels, scaled to standard card width
    public static final float CARD_Y_MARGIN = 5f;                   // in pixels, scaled to standard card height
    public static final float POINT_X_MARGIN = 0f;                  // in pixels, scaled to standard card height
    public static final float POINT_Y_MARGIN = 6f;                  // in pixels, scaled to standard card height
    public static final float POINT_MAX_ICON_SIZE = 0.15f;          // as percentage of standard card width
    public static final float POINT_ICON_MARGIN = 0.025f;           // as percentage of standard card width
    public static final float CARD_NAME_HEIGHT_SIZE = 0.10f;        // as percentage of standard card height
    public static final float CARD_PORTRAIT_ICON_SIZE = 0.75f;      // as percentage of standard card width
    
    public static final float PORTRAIT_ICON_SIZE_MODIFIER = 1.575f; // used to scale up portrait icons, since the actual images have a transparent padding
    public static final float POINT_ICON_SIZE_MODIFIER = 1.2f;      // used to scale up point icons, since the actual images have a transparent padding
    
    public static final float CARD_NAME_Y_MARGIN = 0f;              // in pixels
    public static final float CARD_DESCRIPTION_Y_MARGIN = 3f;       // in pixels
    
    public static final String CARD_NAME_FONT = "consolas";
    public static final int CARD_NAME_FONT_DEFAULT_SIZE = 32;
    
    public static final String CARD_DESCRIPTION_FONT = "consolas";
    public static final int CARD_DESCRIPTION_FONT_DEFAULT_SIZE = 32;
    
    public static final float CARD_TEXT_MAX_WIDTH = ( STANDARD_CARD_WIDTH - (CARD_X_MARGIN*2f) ) * (CARD_DESCRIPTION_FONT_DEFAULT_SIZE / 10f);
    
    public static final String ACTION_DELIMITER = ";;";
    public static final String ACTION_TOKEN_DELIMITER = " ";
    
    public static final char AUTO_SPLIT_GROUP_OPERATOR_OPEN = '<';     // used to open a text groupation when declaring description text
    public static final char AUTO_SPLIT_GROUP_OPERATOR_CLOSE = '>';    // used to close a text groupation when declaring description text
    
    public static final String CARD_BACK_IMG = "cards/card_back";
    public static final String CARD_SHADOW_IMG = "cards/card_back_shadow";
    
    public static final float SHADOW_X_OFFSET = 7.5f;
    public static final float SHADOW_Y_OFFSET = 7.5f;
    
    public static final float CARD_PORTRAIT_BORDER_THICKNESS = 2.5f; // in pixels
    
    
    
    // resource fetching is allowed, because the first time Card.class is loaded
    //    (and shadow and background are initialized) is AFTER ResourceMgr has
    //    already been initialized ...
    private static Image shadow = ResourceManager.getGraphics(CARD_SHADOW_IMG);
    private static Image background = ResourceManager.getGraphics(CARD_BACK_IMG);
    
    
    
    private String id;
    private String name;
    private String card_pack;
    private String description;
    private String icon_name;
    private String sfx_callstring;
    
    private Integer unlock_level;
    
    private Image icon;
    private Color icon_background;
    
    private TargetEnum target_mode;
    private List<CardAction> actions;
    private Map<PointTypeEnum, Integer> point_requirement_map;
    
    private TrueTypeFont name_font;
    private TrueTypeFont text_font;
    
    
    
    public Card () {
        this("","","","","","","","");
    }
    
    public Card (String id, String card_pack, String name, String icon, String target_mode, String action, String description, String sfx) {
        this.id = id;
        this.name = name;
        this.sfx_callstring = sfx;
        this.card_pack = card_pack;
        this.icon_background = new Color (1f,1f,1f,1f);
        this.unlock_level = 0;
        
        this.target_mode = TargetEnum.getTargetModeByName(target_mode);
        
        if (ResourceManager.hasGraphics(icon)) {
            this.icon_name = icon;
            this.icon = ResourceManager.getGraphics(icon);
        }
        
        point_requirement_map = new HashMap<> ();
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum point_type = PointTypeEnum.values()[i];
            point_requirement_map.put(point_type, 0);
        }
        
        name_font = ResourceManager.getFont(CARD_NAME_FONT, CARD_NAME_FONT_DEFAULT_SIZE);
        text_font = ResourceManager.getFont(CARD_DESCRIPTION_FONT, CARD_DESCRIPTION_FONT_DEFAULT_SIZE);
        
        this.setAction(action, false);
        this.description = autoSplitText(description);
    }
    
    
    
    public void render (Graphics g, float x, float y, float scale, boolean played) {
        // if the currently playing player creature cannot pay the card cost, render a darker overlay 
        // ONLY IF THE CURRENT GAMESTATE IS CombatState !
        Color tint = Color.white;
        if (SharedState.isCurrentState(CombatState.class)) {
            if ( (GameplayManager.getCurrentOpponent()==GameplayManager.Opponent.PLAYER && (CombatState.getCurrentCastingCreature()==null || !CombatState.getCurrentCastingCreature().canSpendPoints(this)) )
                    || (played) ) {
                tint = new Color (0.5f, 0.5f, 0.5f, 1f);
            }
        }
        
        // RENDER SHADOW
        if (shadow!=null) {
            float shadow_x = x - ( scale*(STANDARD_SHADOW_WIDTH-STANDARD_CARD_WIDTH)/2f ) + (SHADOW_X_OFFSET*scale);
            float shadow_y = y - ( scale*(STANDARD_SHADOW_HEIGHT-STANDARD_CARD_HEIGHT)/2f ) + (SHADOW_Y_OFFSET*scale);
            shadow.draw(shadow_x, shadow_y, scale*STANDARD_SHADOW_WIDTH, scale*STANDARD_SHADOW_HEIGHT);
        }
        
        // RENDER BACKGROUND
        background.draw(x, y, scale*STANDARD_CARD_WIDTH, scale*STANDARD_CARD_HEIGHT, tint);
        
        // RENDER NAME
        float name_x_offset = CARD_X_MARGIN * scale;
        float name_y_offset = (CARD_NAME_Y_MARGIN + CARD_Y_MARGIN) * scale;
        float name_height = 0f;
        float name_scale = 1f;
        float name_max_width = 0f;
        float name_max_height = ((STANDARD_CARD_HEIGHT - 2*CARD_Y_MARGIN) * CARD_NAME_HEIGHT_SIZE) * scale;
        
        if (!name.trim().isEmpty()) {
            String[] name_lines = name.split("\n");
            
            // scale name based on string width
            for (String line : name_lines)
                name_max_width = Math.max(name_font.getWidth(line), name_max_width);
            if ( name_max_width >= (STANDARD_CARD_WIDTH-2*(CARD_X_MARGIN)-(CARD_X_MARGIN)) )
                name_scale = ( (STANDARD_CARD_WIDTH-2*(CARD_X_MARGIN)) ) / name_max_width;
            
            // scale name based on string height
            for (String line : name_lines)
                name_height += name_font.getHeight(line) * scale;
            if (name_height > name_max_height)
                name_scale = Math.min(name_scale, name_max_height/name_height);
            
            float s = scale*name_scale;
            g.setFont(name_font);
            g.translate(x + name_x_offset, y + name_y_offset);
            g.scale(s, s);
            g.setColor(Color.white);
            g.drawString(name, 0, 0);
            g.scale(1f/s, 1f/s);
            g.translate(-x -name_x_offset, -y -name_y_offset);
        }
        
        // RENDER POINT COST
        float point_x_offset = ( CARD_X_MARGIN + POINT_X_MARGIN ) * scale;
        float point_y_offset = name_height*name_scale + name_y_offset;
        
        int point_sum = 0;
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            point_sum += point_requirement_map.get(type);
        }
        
        float point_margin = (POINT_ICON_MARGIN * (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2)) * scale;
        float point_size = ( (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2 - (POINT_ICON_MARGIN * (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2))*point_sum) / point_sum ) * scale;
        float point_max_size = ( (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2 - (POINT_ICON_MARGIN * (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2))/POINT_MAX_ICON_SIZE) * POINT_MAX_ICON_SIZE ) * scale;
        point_size = Math.min (point_size,point_max_size);
        
        point_y_offset += point_max_size/2f - point_size/2f;
        point_x_offset += point_margin/2f;
        
        for (int i=0;i<PointTypeEnum.values().length;i++) {
            PointTypeEnum type = PointTypeEnum.values()[i];
            Integer points = point_requirement_map.get(type);
            
            for (int j=0;j<points;j++) {
                type.render(g, x+point_x_offset, y+point_y_offset, point_size, point_size);
                point_x_offset += point_margin + point_size;
            }
        }
        
        // RENDER PORTRAIT
        point_y_offset -= point_max_size/2f - point_size/2f; // subtract scaling offset addition for further calculations
        
        float icon_size = ( CARD_PORTRAIT_ICON_SIZE * (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2) ) * scale;
        float icon_x_offset = ( ( (1f - CARD_PORTRAIT_ICON_SIZE) * (STANDARD_CARD_WIDTH - CARD_X_MARGIN*2) )/2f + CARD_X_MARGIN ) * scale;
        float icon_y_offset = point_y_offset + point_max_size + CARD_PORTRAIT_BORDER_THICKNESS*2*scale;
        
        float modified_icon_size = icon_size * PORTRAIT_ICON_SIZE_MODIFIER;
        float modified_icon_x_offset = icon_x_offset - (modified_icon_size - icon_size)/2f;
        float modified_icon_y_offset = icon_y_offset - (modified_icon_size - icon_size)/2f;
        
        // render portrait background
        g.setColor(Color.black);
        g.fillRect(x+icon_x_offset - CARD_PORTRAIT_BORDER_THICKNESS*scale, y+icon_y_offset - CARD_PORTRAIT_BORDER_THICKNESS*scale, icon_size + CARD_PORTRAIT_BORDER_THICKNESS*2*scale, icon_size + CARD_PORTRAIT_BORDER_THICKNESS*2*scale);
        
        Color tinted_icon_background = new Color (icon_background.r*tint.r, icon_background.g*tint.g, icon_background.b*tint.b, icon_background.a*tint.a);
        g.setColor(tinted_icon_background);
        g.fillRect(x+icon_x_offset, y+icon_y_offset, icon_size, icon_size);
        
        if (icon!=null) icon.draw(x+modified_icon_x_offset, y+modified_icon_y_offset, modified_icon_size, modified_icon_size);
        
        if (description.isEmpty()) return;
        
        // RENDER DESCRIPTION
        float text_x_offset = CARD_X_MARGIN * scale;
        float text_y_offset = icon_y_offset + icon_size + (CARD_DESCRIPTION_Y_MARGIN + CARD_Y_MARGIN)*scale;
        float remaining_card_space = STANDARD_CARD_HEIGHT*scale - text_y_offset - (2*CARD_Y_MARGIN*scale);
        
        String[] text_lines = description.split("\n");
        
        float text_height = 0f;
        for (String line : text_lines)
            text_height += text_font.getHeight(line) * scale;
        float graphics_scale = ( Math.min(1.0f, (remaining_card_space*scale) / text_height) );
        
        float text_width = 0f;
        for (String line : text_lines)
            text_width = Math.max(text_width, text_font.getWidth(line));
        graphics_scale = ( Math.min(graphics_scale, (( (STANDARD_CARD_WIDTH - 2*CARD_X_MARGIN) * scale) / text_width) ) );
        
        g.setFont(text_font);
        g.translate(x + text_x_offset, y + text_y_offset);
        g.scale(graphics_scale, graphics_scale);
        g.setColor(Color.white);
        float line_x_mod, line_y_mod = 0f;
        for (int i=0;i<text_lines.length;i++) {
            String line = text_lines[i];
//            line_x_mod = (CARD_X_MARGIN*scale + (STANDARD_CARD_WIDTH/2f)*scale - (text_font.getWidth(line)/2f)*graphics_scale);
            line_x_mod = (((STANDARD_CARD_WIDTH*scale - CARD_X_MARGIN*2*scale)/graphics_scale)/2f - (text_font.getWidth(line))/2f);
            g.drawString(line, line_x_mod, line_y_mod);
            line_y_mod += text_font.getHeight(line);
        }
        g.scale(1/(graphics_scale), 1/(graphics_scale));
        g.translate(-x -text_x_offset, -y -text_y_offset);
    }
    
    
    
    public void assignId (String new_id) {
        if (id==null || id.isEmpty())
            this.id = new_id;
    }
    
    public void assignIdOverwrite (String new_id) {
        this.id = new_id;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    private void setAction (String action, boolean silently) {
        this.actions = Card.parseActions(action, silently);
    }
    
    public void setAction (String action) {
        this.setAction(action, false);
    }
    
    public void setSfxCallstring (String sfx) {
        this.sfx_callstring = sfx;
    }
    
    public void setUnlockLevel (Integer unlock_lvl) {
        this.unlock_level = unlock_lvl;
    }
    
    public void silentSetAction (String action) {
        this.setAction(action, true);
    }
    
    public static List<CardAction> parseActions (String action, boolean silently) {
        List<CardAction> result = new ArrayList<> ();
        
        if (!action.isEmpty()) {
            String[] act_line = action.split(ACTION_DELIMITER);
            for (int i=0;i<act_line.length;i++) {
                try {
                    List<String> action_tokens = new ArrayList<> ( Arrays.asList(act_line[i].split(ACTION_TOKEN_DELIMITER)) );
                    for (int j=0;j<action_tokens.size();j++) {
                        if (action_tokens.get(j).trim().isEmpty())
                            action_tokens.remove(j);
                        else
                            action_tokens.set(j, action_tokens.get(j).trim());
                    }
                    
                    String action_method_name = action_tokens.get(0);
                    Method action_method = CardActionHandler.class.getMethod(action_method_name, Creature.class, Card.class, List.class, Creature.class);
                    
                    result.add(new CardAction (action_method, action_tokens.subList(1, action_tokens.size())));
                } catch (NoSuchMethodException | SecurityException ex) {
                    if (!silently) Log.err(ex);
                }
            }
        }
        
        if (result.isEmpty() && !action.isEmpty())
            Log.err("Returning empty action set after parsing?","\tAction string: '"+action+"'");
        
        return result;
    }
    
    public void setDescription (String description) {
        this.description = autoSplitText(description);
    }
    
    public void setPointCost (PointTypeEnum point_type, int amount) {
        point_requirement_map.put(point_type, amount);
    }
    
    public void setPointCostMap (HashMap<PointTypeEnum, Integer> point_cost) {
        for (PointTypeEnum type : PointTypeEnum.values()) {
            if (!point_cost.containsKey(type))
                this.point_requirement_map.put(type, 0);
            else
                this.point_requirement_map.put(type, point_cost.get(type));
        }
    }
    
    public void setIcon (String icon) {
        if (ResourceManager.hasGraphics(icon)) {
            this.icon_name = icon;
            this.icon = ResourceManager.getGraphics(icon);
        }
    }
    
    public void setIconBackground (float r, float g, float b, float a) {
        this.icon_background = new Color (r,g,b,a);
    }
    
    public void setIconBackground (Color color) {
        this.icon_background = color;
    }
    
    public void setCardPack (String card_pack) {
        this.card_pack = card_pack;
    }
    
    public void setCardPack (int index) {
        this.card_pack = CardLibrary.getLoadedCardPacks() [index];
    }
    
    
    
    public String getId () {
        return id;
    }
    
    public String getName () {
        return name;
    }
    
    public String getIconName () {
        return icon_name;
    }
    
    public Map<PointTypeEnum, Integer> getPointPool () {
        return point_requirement_map;
    }
    
    public String getCardPack () {
        return card_pack;
    }
    
    public int getCardPackIndex () {
        String[] all_packs = CardLibrary.getLoadedCardPacks();
        for (int i=0;i<all_packs.length;i++)
            if (all_packs[i].equals(this.card_pack))
                return i;
        return -1;
    }
    
    public int getUnlockLevel () {
        return unlock_level;
    }
    
    public TargetEnum getTargetMode () {
        return target_mode;
    }
    
    public List<CardAction> getActions () {
        return actions;
    }
    
    public String getSfxCallstring () {
        return sfx_callstring;
    }
    
    
    
    private String autoSplitText (String text) {
        String temp, temp_line = "";
        String result = "";
        
        for (int i=0;i<text.length();i++) {
            int j=i;
            boolean grouping=false;
            boolean operator=false;
            temp = "";
            
            for (;j<text.length();j++) {
                char c = text.charAt(j);
                
                if (!grouping && Character.isWhitespace(c)) {
                    break;
                } else if (c == AUTO_SPLIT_GROUP_OPERATOR_OPEN) {
                    grouping=true;
                } else if ( grouping && (c == AUTO_SPLIT_GROUP_OPERATOR_CLOSE) ) {
                    grouping=false;
                } else {
                    temp += c;
                }
            }
            
            i=j;
            
            if ((float)( text_font.getWidth( (temp_line.isEmpty() ? "" : (temp_line+" ") )+temp) ) >= CARD_TEXT_MAX_WIDTH) {
                result += temp_line + "\n";
                temp_line = temp;
            } else {
                if (!temp_line.isEmpty()) temp_line += " ";
                temp_line += temp;
            }
        }
        if (!temp_line.trim().isEmpty())
            result += temp_line;
        
        return result.trim().replace("|n", "\n");
    }
    
    public void play (SpecialEffectSystem sfx, Creature source, Creature... targets) {
        if (!source.spendPoints(this)) {
            Log.err("Cannot cast card '"+this.name+"' because caster '"+source.getName()+"' doesn't have enough points!");
            return;
        }
        
        sfx.addSfx(this, source, targets);
        sfx.playSfx();
    }
    
    public void play (SpecialEffectSystem sfx, Creature source, List<Creature> targets) {
        if (!source.spendPoints(this)) {
            Log.err("Cannot cast card '"+this.name+"' because caster '"+source.getName()+"' doesn't have enough points!");
            return;
        }
        
        sfx.addSfx(this, source, targets);
        sfx.playSfx();
    }
    
    
    
    @Override
    public String toString () {
        String point_string = "";
        for (PointTypeEnum type : PointTypeEnum.values()) {
            if (point_requirement_map.get(type) > 0)
                point_string += type.code + " " + point_requirement_map.get(type).toString() + " ";
        }
        point_string = point_string.trim();
        
        String action_string = "";
        for (int i=0;i<actions.size();i++) {
            if (!action_string.isEmpty()) action_string += ACTION_DELIMITER;
            action_string += actions.get(i).toString();
        }
        action_string = action_string.trim();
        
        String color_string = "";
        color_string += String.format("%.2f",this.icon_background.r) + " ";
        color_string += String.format("%.2f",this.icon_background.g) + " ";
        color_string += String.format("%.2f",this.icon_background.b) + " ";
        color_string += String.format("%.2f",this.icon_background.a);
        
        String contents = "";
        contents += "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_NEW_CARD + CardLibrary.PARSE_DELIMITER + " " + this.id + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_NAME + CardLibrary.PARSE_DELIMITER + " " + this.name + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_ICON + CardLibrary.PARSE_DELIMITER + " " + this.icon_name + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_TARGET_MODE + CardLibrary.PARSE_DELIMITER + " " + this.target_mode.name().toLowerCase() + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_DESCRIPTION + CardLibrary.PARSE_DELIMITER + " " + this.description + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_ACTION + CardLibrary.PARSE_DELIMITER + " " + action_string + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_POINTS + CardLibrary.PARSE_DELIMITER + " " + point_string + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_COLOR + CardLibrary.PARSE_DELIMITER + " " + color_string + "\n";
        contents += CardLibrary.PARSE_KEYWORD_SET_SFX + CardLibrary.PARSE_DELIMITER + " " + sfx_callstring + "\n";
        contents += "\n";
        
        return contents;
    }
    
}
