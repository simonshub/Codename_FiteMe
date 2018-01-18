/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.utils.Log;
import org.simon.src.utils.ResourceManager;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public enum PointTypeEnum {
    
    ATTACK  ("atk", "Attack",  "points/attack",    1f,0f,0f),  // basic point   - basic points are used by all classes, but
    DEFENCE ("def", "Defence", "points/defence",   0f,0f,1f),  // basic point     warriors, rogues and duelists use these
    AGILITY ("agi", "Agility", "points/agility",   1f,1f,0f),  // basic point     more extensively since they do not have
                                                               //                 special points.
    
    ARCANE  ("arc", "Arcane",  "points/arcane",    .5f,.5f,1f),    // special point for battle mages, wizards and elementalists
    NATURE  ("nat", "Nature",  "points/nature",    .2f,1f,.3f),    // special point for druids, rangers and shamans
    DIVINE  ("div", "Divine",  "points/divine",     1f,1f,.6f),    // special point for clerics, priests and paladins
    DEATH   ("dth", "Death",   "points/dark",      .4f,.4f,.4f),   // special point for death knights, necromancers and assassins
    
    // - some classes mix special points, like heirophants (arcane/divine),
    //   witches (nature/arcane), warlocks (arcane/dark), etc ...
    
    ;
    
    public static final int NOMINAL_ICON_SIZE = 64;
    public static final Image POINT_ICON_BACKGROUND = ResourceManager.getGraphics("points/background");
    
    
    
    public final Image icon;
    public final Color color;
    public final String code;
    public final String display_name;
    
    PointTypeEnum (String code, String display_name, String icon, float r, float g, float b) {
        this.code = code;
        this.color = new Color (r,g,b,1f);
        this.display_name = display_name;
        this.icon = ResourceManager.getGraphics(icon);
    }
    
    public void render (Graphics g, float x, float y, float width, float height) {
        render(g,x,y,width,height,1f);
    }
    
    public void renderUsed (Graphics g, float x, float y, float width, float height) {
        renderUsed(g,x,y,width,height,1f);
    }
    
    public void render (Graphics g, float x, float y, float width, float height, float alpha) {
        Color render_color = new Color (color.r, color.g, color.b, alpha);
        POINT_ICON_BACKGROUND.draw(x, y, width, height, render_color);
        icon.draw(x - (width*Card.POINT_ICON_SIZE_MODIFIER - width)/2f,
                  y - (height*Card.POINT_ICON_SIZE_MODIFIER - height)/2f,
                  width * Card.POINT_ICON_SIZE_MODIFIER,
                  height * Card.POINT_ICON_SIZE_MODIFIER, render_color
                );
    }
    
    public void renderUsed (Graphics g, float x, float y, float width, float height, float alpha) {
        Color used_color = new Color (0f, 0f, 0f, alpha);
        
        POINT_ICON_BACKGROUND.draw(x, y, width, height, used_color);
        icon.draw(x - (width*Card.POINT_ICON_SIZE_MODIFIER - width)/2f,
                  y - (height*Card.POINT_ICON_SIZE_MODIFIER - height)/2f,
                  width * Card.POINT_ICON_SIZE_MODIFIER,
                  height * Card.POINT_ICON_SIZE_MODIFIER, used_color
                );
    }
    
    
    
    public static PointTypeEnum getByCode (String code) {
        PointTypeEnum[] values = PointTypeEnum.values();
        
        for (int i=0;i<values.length;i++) {
            if (code.equals(values[i].code))
                return values[i];
        }
        
        Log.err("No point type with code '"+code+"'!");
        return null;
    }
    
}
