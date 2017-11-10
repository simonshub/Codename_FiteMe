/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

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
    
    // insert card on-play methods here ...
    
    public static void damage (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for damage?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int dmg = SlickUtils.rand(min, max);
                    target.reduceHealth(dmg, source);
                    target.addFloatingText(String.valueOf(dmg), Color.red);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int dmg = SlickUtils.rand(min, max);
                target.reduceHealth(dmg, source);
                target.addFloatingText(String.valueOf(dmg), Color.red);
            }
        }
    }
    
    public static void heal (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for heal?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int heal = SlickUtils.rand(min, max);
                    target.restoreHealth(heal, source);
                    target.addFloatingText(String.valueOf(heal), Color.green);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int heal = SlickUtils.rand(min, max);
                target.restoreHealth(heal, source);
                target.addFloatingText(String.valueOf(heal), Color.green);
            }
        }
    }
    
    public static void lifesteal (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for lifesteal?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int dmg = SlickUtils.rand(min, max);
                    target.reduceHealth(dmg, target);
                    source.restoreHealth(dmg, source);
                    target.addFloatingText(String.valueOf(dmg), Color.red);
                    source.addFloatingText(String.valueOf(dmg), Color.green);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int dmg = SlickUtils.rand(min, max);
                target.reduceHealth(dmg, source);
                source.restoreHealth(dmg, source);
                target.addFloatingText(String.valueOf(dmg), Color.red);
                source.addFloatingText(String.valueOf(dmg), Color.green);
            }
        }
    }
    
    public static void disable (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for disable?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int dur = SlickUtils.rand(min, max);
                    target.disable(dur);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int dur = SlickUtils.rand(min, max);
                target.disable(dur);
            }
        }
    }
    
    public static void add_armor (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for add_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int armor = SlickUtils.rand(min, max);
                    target.addArmor(armor);
                    target.addFloatingText(String.valueOf(armor), Color.white);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int armor = SlickUtils.rand(min, max);
                target.addArmor(armor);
                target.addFloatingText(String.valueOf(armor), Color.white);
            }
        }
    }
    
    public static void remove_armor (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for remove_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0)) + source.getAttackModifier();
        int max = min;
        int instance_count = 1;
        boolean aoe = true;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + source.getAttackModifier();
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        if (args.size()>=4)
            aoe = Boolean.parseBoolean(args.get(3));
        
        for (int i=0;i<instance_count;i++) {
            if (aoe) {
                for (Creature target : targets) {
                    int armor = SlickUtils.rand(min, max);
                    target.removeArmor(armor);
                    target.addFloatingText(String.valueOf("-"+armor), Color.gray);
                }
            } else {
                Creature target = (Creature) SlickUtils.randListObject(targets);
                int armor = SlickUtils.rand(min, max);
                target.removeArmor(armor);
                target.addFloatingText(String.valueOf("-"+armor), Color.gray);
            }
        }
    }
    
    public static void status (Creature source, Card parent, List<String> args, List<Creature> targets) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for add_status_effect?");
            return;
        }
        
        int dur = Integer.parseInt(args.get(0));
        args.remove(0);
        int split_index = 0;
        
        for (int i=0;i<args.size();i++) {
            if (args.get(i).equals(StatusEffect.STATUS_EFFECT_DELIMITER)) {
                split_index = i;
                break;
            }
        }
        
        List<String> action_list = args.subList(0, split_index);
        List<String> status_list = args.subList(split_index+1, args.size());
        String action_string = SlickUtils.getListAsStringList(action_list, Card.ACTION_TOKEN_DELIMITER);
        String status_string = SlickUtils.getListAsStringList(status_list, Card.ACTION_TOKEN_DELIMITER);
        
        for (int i=0;i<targets.size();i++) {
            StatusEffect status = new StatusEffect (parent, source, targets.get(i), dur, action_string, status_string);
            targets.get(i).addStatusEffect(status);
        }
    }
    
    public static void alt_card (Creature source, Card parent, List<String> args, List<Creature> targets) {
        String new_card = args.get(0);
        source.replaceCard(parent, CardLibrary.getCard(new_card));
    }
    
}
