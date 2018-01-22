/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.gameplay.player.PlayerCharacter;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author XyRoN
 */
public class SavedState implements Serializable {
    
    private PlayerState player_state;
    private BoardState board_state;
    
    protected SavedState () { }
    
    
    
    protected void snapshot () {
        player_state = new PlayerState ();
        board_state = new BoardState ();
        
        player_state.score = Player.getScore();
        
        player_state.hand = new ArrayList<> ();
        player_state.played_hand = new ArrayList<> ();
        List<GuiElement> elements = CombatState.gui.getElements("card_slot");
        for (int i=0;i<elements.size();i++) {
            player_state.hand.add( elements.get(i).getCard().getId() );
            player_state.played_hand.add( elements.get(i).getCardPlayed() );
        }
        
        player_state.party = new ArrayList<> ();
        List<PlayerCharacter> party = Player.getParty();
        for (int i=0;i<party.size();i++) {
            PlayerCharacter plr_char = party.get(i);
            Creature plr_char_creature = plr_char.getCreature();
            
            CharacterState char_state = new CharacterState ();
            char_state.character_class = plr_char.getCharacterClass().getId();
            char_state.character_level = plr_char.getLevel();
            char_state.creature = new CreatureState();
            char_state.creature.fromCreature(plr_char_creature);
            
            player_state.party.add(char_state);
        }
        
        board_state.wave = GameplayManager.getCurrentWave();
        board_state.level_type = GameplayManager.getCurrentLevelType().getId();
        board_state.total_difficulty_so_far = GameplayManager.getCurrentTotalDifficulty();
        
        board_state.enemy_board = new ArrayList<> ();
        List<GuiElement> enemy_elements = CombatState.gui.getElements("_enemy_");
        for (int i=0;i<enemy_elements.size();i++) {
            Creature enemy = enemy_elements.get(i).getCreature();
            if (enemy!=null && !enemy.isDead()) {
                CreatureState enemy_state = new CreatureState();
                enemy_state.fromCreature(enemy);
                board_state.enemy_board.add(enemy_state);
            }
        }
    }
    
    
    
    protected void apply () {
        board_state.apply();
        player_state.apply();
        
        board_state.applyStatusEffects();
        player_state.applyStatusEffects();
    }
    
}
