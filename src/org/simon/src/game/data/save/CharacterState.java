/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.player.PlayerCharacter;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;

/**
 *
 * @author emil.simon
 */
public class CharacterState implements Serializable {
    protected int character_level;
    protected String character_class;
    protected CreatureState creature;
    
    protected PlayerCharacter asPlayerCharacter () {
        PlayerCharacter player_char=null;
        PlayerCharacterClass char_class = GameplayManager.getPlayerCharacterClass(character_class);
        
        player_char = new PlayerCharacter (char_class);
        player_char.setLevel(character_level);
        
        final Creature player_char_creature = player_char.getCreature();
        creature.applyToCreature(player_char_creature);
        
        return player_char;
    }
}
