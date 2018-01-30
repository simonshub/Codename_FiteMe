/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.simon.src.game.data.gameplay.creatures.Creature;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.combat.CombatState;
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
        
        int atk_mod = source.getAttackModifier();
        source.consumeAttackMod();
        
        int min = Integer.parseInt(args.get(0)) + atk_mod;
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + atk_mod;
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int dmg = Math.max(0,SlickUtils.rand(min, max));
            target.reduceHealth(dmg, source);
            target.addFloatingText(String.valueOf(dmg), Color.red);
        }
    }
    
    public static void heal (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for heal?");
            return;
        }
        
        int atk_mod = source.getAttackModifier();
        source.consumeAttackMod();
        
        int min = Integer.parseInt(args.get(0)) + atk_mod;
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + atk_mod;
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int heal = Math.max(0,SlickUtils.rand(min, max));
            target.restoreHealth(heal, source);
            target.addFloatingText(String.valueOf(heal), Color.green);
        }
    }
    
    public static void lifesteal (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for lifesteal?");
            return;
        }
        
        int atk_mod = source.getAttackModifier();
        source.consumeAttackMod();
        
        int min = Integer.parseInt(args.get(0)) + atk_mod;
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1)) + atk_mod;
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int dmg = Math.max(0,SlickUtils.rand(min, max));
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
        
        int min = Integer.parseInt(args.get(0));
        int max = min;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1));
        
        int dur = SlickUtils.rand(min, max);
        target.disable(dur);
    }
    
    public static void add_armor (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for add_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0));
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1));
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int armor = SlickUtils.rand(min, max);
            target.addArmor(armor);
            target.addFloatingText(String.valueOf(armor), Color.white);
        }
    }
    
    public static void add_atkmod (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for add_atkmod?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0));
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1));
        if (args.size()>=3)
            instance_count = Integer.parseInt(args.get(2));
        
        for (int i=0;i<instance_count;i++) {
            int atk_mod = SlickUtils.rand(min, max);
            target.addAttackMod(atk_mod);
            target.addFloatingText( (atk_mod>0 ? "+" : "") + String.valueOf(atk_mod), Color.orange);
        }
    }
    
    public static void remove_armor (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for remove_armor?");
            return;
        }
        
        int min = Integer.parseInt(args.get(0));
        int max = min;
        int instance_count = 1;
        
        if (args.size()>=2)
            max = Integer.parseInt(args.get(1));
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
    
    public static void summon (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for summon?");
            return;
        }
        
        String creature_name = args.get(0);
        Creature creature = null;
        
        if (creature_name.equals("parent")) {
            creature = source.getParent();
        } else {
            creature = CreatureLibrary.getCreature(creature_name);
        }
        
        if (creature != null) {
            // determine if there's an empty enemy slot
            List<GuiElement> creature_slots = CombatState.gui.getElements("_enemy_");
            List<GuiElement> available_creature_slots = new ArrayList<> ();
            for (int i=0;i<creature_slots.size();i++) {
                if (creature_slots.get(i).getCreature() == null || creature_slots.get(i).getCreature().isDead()) {
                    available_creature_slots.add(creature_slots.get(i));
                }
            }
            
            if (available_creature_slots.isEmpty()) {
                Log.err("Tried to summon creature with ID '"+creature_name+"', but no free creature slots were found");
            } else {
                GuiElement el = (GuiElement) SlickUtils.randListObject(available_creature_slots);
                el.setCreature(new Creature (creature));
                GameplayManager.addEnemy(el);
                creature.addFloatingText("Summon", Color.red);
            }
        }
    }
    
    public static void kill (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for left?");
            return;
        }
        
        boolean only_if_disabled = Boolean.parseBoolean(args.get(0));
        boolean only_if_damaged = Boolean.parseBoolean(args.get(1));
        
        boolean satisfies_cond = true;
        
        if (only_if_disabled) satisfies_cond = satisfies_cond && target.isDisabled();
        if (only_if_damaged) satisfies_cond = satisfies_cond && ( target.getCurrentHealthPercent() >= 0.5f );
        
        int dmg = 0;
        if (satisfies_cond) dmg = target.getCurrentHealth();
        target.reduceHealth(dmg, source);
    }
    
    public static void left (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for left?");
            return;
        }
        
        String method_name = args.get(0);
        
        try {
            Method m = CardActionHandler.class.getMethod(method_name, Creature.class, Card.class, List.class, Creature.class);
            String el_name = target.getGuiElement().getName();
            
            int index = Integer.parseInt( el_name.substring(el_name.length()-1, el_name.length()) ) - 1;
            GuiElement left_el = CombatState.gui.getElement(el_name.substring(0,el_name.length()-1) + index);
            
            if (left_el==null || left_el.getCreature()==null || left_el.getCreature().isDead()) return;
            
            args.remove(0);
            target = left_el.getCreature();
            
            m.invoke(null, source, parent, args, target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err("Could not invoke card action for left neighbour; "+SlickUtils.Strings.concatList(args, ", "));
            Log.err(ex);
        }
    }
    
    public static void right (Creature source, Card parent, List<String> args, Creature target) {
        if (args.isEmpty()) {
            Log.err("No arguments passed for right?");
            return;
        }
        
        String method_name = args.get(0);
        
        try {
            Method m = CardActionHandler.class.getMethod(method_name, Creature.class, Card.class, List.class, Creature.class);
            String el_name = target.getGuiElement().getName();
            
            int index = Integer.parseInt( el_name.substring(el_name.length()-1, el_name.length()) ) + 1;
            GuiElement right_el = CombatState.gui.getElement(el_name.substring(0,el_name.length()-1) + index);
            
            if (right_el==null || right_el.getCreature()==null || right_el.getCreature().isDead()) return;
            
            args.remove(0);
            target = right_el.getCreature();
            
            m.invoke(null, source, parent, args, target);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Log.err("Could not invoke card action for right neighbour; "+SlickUtils.Strings.concatList(args, ", "));
            Log.err(ex);
        }
    }
    
}
