/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

/**
 *
 * @author XyRoN
 */
public enum TargetEnum {
    SINGLE_ENEMY,
    SINGLE_ALLY,
    ALL_ENEMIES,
    ALL_ALLIES,
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
}
