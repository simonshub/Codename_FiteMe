/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.simon.src.game.data.gameplay.PointTypeEnum;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;

/**
 *
 * @author emil.simon
 */
public class PlayerCharacter {
    
    public static final int MAX_LEVEL = 10;
    public static final int STARTING_POINTS = 3;
    public static final int POINTS_PER_LEVEL = 1;
    
    public static final String PLAYER_CHARACTER_CREATURE_ID_PREFIX = "char_";
    
    private final List<Card> unlocked_card_list;
    private final PlayerCharacterClass character_class;
    
    private int current_level;
    private Creature creature;
    
    public PlayerCharacter (PlayerCharacterClass character_class) {
        this.current_level = 1;
        this.unlocked_card_list = new ArrayList<> (character_class.getCardListForLevel(current_level));
        this.character_class = character_class;
    }
    
    public int getLevel () {
        return current_level;
    }
    
    public void levelUp (Card new_card) {
        current_level++;
        unlocked_card_list.add(new_card);
    }
    
    public final Creature getCreature () {
        if (creature==null) {
            creature = new Creature (character_class.getCreatureParent(current_level));
            creature.setId(PLAYER_CHARACTER_CREATURE_ID_PREFIX + creature.getId());
        }
        return creature;
    }
    
    public HashMap<PointTypeEnum, Integer> getPointPool () {
        return this.character_class.getPointPoolForLevel(current_level);
    }
    
}
