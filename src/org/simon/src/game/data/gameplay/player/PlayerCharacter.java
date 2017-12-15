/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        this.unlocked_card_list = new ArrayList<> ();
        this.character_class = character_class;
    }
    
    public Creature getCreature () {
        if (creature==null) makeCreature();
        return creature;
    }
    
    public HashMap<PointTypeEnum, Integer> getPointPool () {
        return this.character_class.getPointPoolForLevel(current_level);
    }
    
    private Creature makeCreature () {
        Creature c = new Creature ();
        
        c.setIcon(character_class.getGraphics());
        c.setId(PLAYER_CHARACTER_CREATURE_ID_PREFIX + character_class.getName().toLowerCase().replace(" ", "_"));
        c.setPoints(character_class.getPointPoolForLevel(current_level));
        c.setName(character_class.getName());
        c.setHealth(character_class.getHealthForLevel(current_level));
        c.setParent(null);
        
        return c;
    }
    
}
