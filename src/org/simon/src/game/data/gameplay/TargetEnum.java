/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.simon.src.game.data.gameplay.creatures.Creature;

/**
 *
 * @author XyRoN
 */
public enum TargetEnum {
    SINGLE_ENEMY,
    SINGLE_ALLY,
    ALL_ENEMIES,
    ALL_ALLIES,
    SELF,
    ALL,
    
    ;
    
    public static TargetEnum getTargetModeByName (String target_mode) {
        for (TargetEnum target : TargetEnum.values()) {
            if (target.name().equalsIgnoreCase(target_mode)) {
                return target;
            }
        }
        return TargetEnum.SINGLE_ENEMY;
    }
    
    public static List<Creature> getTargetList (TargetEnum mode, Creature caster, Creature primary_target, List<Creature> enemies, List<Creature> allies) {
        List<Creature> result_list = new ArrayList<> ();
        
        switch (mode) {
            case SINGLE_ENEMY :
                if (GameplayManager.isCreatureEnemy(primary_target) && !primary_target.isDead()) result_list.add(primary_target);
                break;
            case SINGLE_ALLY :
                if (!GameplayManager.isCreatureEnemy(primary_target) && !primary_target.isDead()) result_list.add(primary_target);
                break;
            case ALL_ENEMIES :
                result_list.addAll(enemies.stream().filter(creature -> !creature.isDead()).collect(Collectors.toList()));
                break;
            case ALL_ALLIES :
                result_list.addAll(allies.stream().filter(creature -> !creature.isDead()).collect(Collectors.toList()));
                break;
            case SELF :
                result_list.add(caster);
                break;
            case ALL :
                result_list.addAll(enemies);
                result_list.addAll(allies);
                break;
        }
        
        return result_list;
    }
}
