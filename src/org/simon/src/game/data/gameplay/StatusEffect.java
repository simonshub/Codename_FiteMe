/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.util.List;
import java.util.regex.Pattern;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardAction;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.sfx.SpecialEffectCallback;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.utils.Log;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class StatusEffect {
    
    public static final int DISPLAY_STRING_ARG_LENGTH = 4;
    public static final String STATUS_EFFECT_DELIMITER = "$";
    
    private int counter;
    
    private final Creature target;
    
    private final SpecialEffectCallback callback;
    
    private final String display_name;
    private final String display_description;
    private final String special_effect_string;
    private final String display_icon;
    
    private final String source_action_string;
    private final String source_display_string;
    
    
    
    public StatusEffect (Card parent, Creature source, Creature target, int counter, String action, String display_string) {
        this.source_action_string = action;
        this.source_display_string = display_string;
        
        this.target = target;
        this.counter = counter;
        
        this.callback = new SpecialEffectCallback (parent, source, target);
        
        String[] display_string_args = new String [] { "error","error","","" };
        String[] temp_args = display_string.split(Pattern.quote(STATUS_EFFECT_DELIMITER));
        if (temp_args.length != DISPLAY_STRING_ARG_LENGTH) {
            Log.err("Erroneous special effect display string '"+display_string+"'; Found "+temp_args.length+" args, instead of "+DISPLAY_STRING_ARG_LENGTH);
        } else {
            display_string_args = SlickUtils.Strings.trimAll(temp_args);
        }
        
        this.display_name = display_string_args[0];
        this.display_description = display_string_args[1];
        this.special_effect_string = display_string_args[2];
        this.display_icon = display_string_args[3];
    }
    
    
    
    public void turnTick (SpecialEffectSystem sfx) {
        counter--;
        sfx.addSfx(this, target, target);
    }
    
    public boolean isDone () {
        return counter<=0;
    }
    
    public Card getParent () {
        return callback.getParent();
    }
    
    public Creature getSource () {
        return callback.getSource();
    }
    
    public int getCounter () {
        return counter;
    }
    
    public Creature getTarget () {
        return callback.getTarget();
    }
    
    public List<CardAction> getActions () {
        return Card.parseActions(source_action_string, true);
    }
    
    public String getDisplayName () {
        return display_name;
    }
    
    public String getDisplayIcon () {
        return display_icon;
    }
    
    public String getDisplayDescription () {
        return display_description;
    }
    
    public String getSfxString () {
        return special_effect_string;
    }
    
    public String getOriginalActionString () {
        return source_action_string;
    }
    
    public String getOriginalDisplayString () {
        return source_display_string;
    }
    
    
    
    @Override
    public String toString () {
        String contents = "";
        contents += " ( ";
        contents += callback.toString();
        contents += " [ "+"name:"+display_name+" ] ";
        contents += " [ "+"description:"+display_description+" ] ";
        contents += " [ "+"sfx:"+special_effect_string+" ] ";
        contents += " ) ";
        return contents;
    }
    
}
