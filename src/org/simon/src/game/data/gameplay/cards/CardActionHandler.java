/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

import java.lang.reflect.Method;
import org.simon.src.game.data.gameplay.creatures.Creature;
import java.util.List;
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.utils.Log;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class CardActionHandler {
    
    public static final String MULTICALL_DELIMITER = ";";
    
    
    
    private CardActionHandler () { }
    
    public static String[] getAllMethodNames () {
        Method[] methods = CardActionHandler.class.getDeclaredMethods();
        String[] result = new String [methods.length];
        
        for (int i=0;i<methods.length;i++) {
            result[i] = methods[i].getName();
        }
        
        return result;
    }
    
    // CARD ACTIONS
    
    // insert card on-play methods here ...
    
    public static void damage (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for damage?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int dmg = SlickUtils.rand(min, max);
            target.reduceHealth(dmg, source);
            target.addFloatingText(String.valueOf(dmg), Color.red);
        }
    }
    
    public static void heal (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for heal?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int heal = SlickUtils.rand(min, max);
            target.restoreHealth(heal, source);
            target.addFloatingText(String.valueOf(heal), Color.green);
        }
    }
    
    public static void lifesteal (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for lifesteal?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int dmg = SlickUtils.rand(min, max);
            target.reduceHealth(dmg, target);
            source.restoreHealth(dmg, source);
            target.addFloatingText(String.valueOf(dmg), Color.red);
            source.addFloatingText(String.valueOf(dmg), Color.green);
        }
    }
    
    public static void disable (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for disable?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int dur = SlickUtils.rand(min, max);
            target.disable(dur);
        }
    }
    
    public static void add_armor (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for add_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int armor = SlickUtils.rand(min, max);
            target.addArmor(armor);
            target.addFloatingText(String.valueOf(armor), Color.white);
        }
    }
    
    public static void remove_armor (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for remove_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int armor = SlickUtils.rand(min, max);
            target.removeArmor(armor);
            target.addFloatingText(String.valueOf("-"+armor), Color.gray);
        }
    }
    
    public static void status (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for status?");
            return;
        }
        
        int dur = Integer.parseInt(args.get(0));
        int split_index = 0;
        
        for (int i=1;i<args.size();i++) {
            if (args.get(i).equals(StatusEffect.STATUS_EFFECT_DELIMITER)) {
                split_index = i;
                break;
            }
        }
        
        List<String> action_list = args.subList(1, split_index);
        List<String> status_list = args.subList(split_index+1, args.size());
        String action_string = SlickUtils.getListAsStringList(action_list, Card.ACTION_TOKEN_DELIMITER);
        String status_string = SlickUtils.getListAsStringList(status_list, Card.ACTION_TOKEN_DELIMITER);
        
        StatusEffect status = new StatusEffect (parent, source, target, dur, action_string, status_string);
        target.addStatusEffect(status);
    }
    
    public static void alt_card (Creature source, Card parent, List<String> args, Creature target) {
        String new_card = args.get(0);
        // changes the old card to the new one...
    }
    
}
