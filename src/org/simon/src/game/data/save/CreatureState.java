/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.creatures.CreatureLibrary;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author emil.simon
 */

        
public class CreatureState implements Serializable {
    protected int armor;
    protected int current_health;
    protected String gui_element;
    protected String parent_creature;
    protected ArrayList<StatusEffectState> status_list;
    protected HashMap<PointTypeEnum, Integer> used_point_map;

    protected void fromCreature (final Creature creature) {
        this.armor = creature.getArmor();
        this.current_health = creature.getCurrentHealth();
        this.gui_element = creature.getGuiElement().getName();

        this.used_point_map = (HashMap<PointTypeEnum, Integer>) creature.getUsedPointPool();

        if (creature.isPlayerCharacter()) {
            this.parent_creature = "";
        } else {
            this.parent_creature = creature.getParent().getId();
        }

        List<StatusEffect> status_effects = creature.getStatusEffects();
        this.status_list = new ArrayList<> ();
        for (int i=0;i<status_effects.size();i++) {
            StatusEffectState status_effect_state = new StatusEffectState ();
            status_effect_state.fromStatusEffect(status_effects.get(i));
            this.status_list.add(status_effect_state);
        }
    }

    public Creature asCreature () {
        Creature creature = null;

        if (!"".equals(parent_creature)) {
            creature = new Creature (CreatureLibrary.getCreature(parent_creature));
            creature.setArmor(armor);
            creature.setCurrentHealth(current_health);
            creature.setUsedPointPool(used_point_map);
            CombatState.gui.getElement(gui_element).setCreature(creature);
        }

        return creature;
    }
    
    protected void applyToCreature (final Creature creature) {
        creature.setArmor(armor);
        creature.setCurrentHealth(current_health);
        creature.setUsedPointPool(used_point_map);
        CombatState.gui.getElement(gui_element).setCreature(creature);
    }

    protected void applyStatusEffects () {
        for (StatusEffectState status_effect_state : status_list) {
            status_effect_state.apply();
        }
    }
}

