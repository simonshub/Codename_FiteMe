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
import org.simon.src.game.data.gameplay.cards.CardPool;
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
    
    private class StatusEffectState implements Serializable {
        protected int counter;
        protected String which_card;
        protected String caster_gui_element;
        protected String target_gui_element;
        
        protected void fromStatusEffect (final StatusEffect status_effect) {
            this.counter = status_effect.getCounter();
            this.which_card = status_effect.getParent().getId();
            this.caster_gui_element = status_effect.getSource().getGuiElement().getName();
            this.target_gui_element = status_effect.getTarget().getGuiElement().getName();
        }
    }
    
    private class CreatureState implements Serializable {
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
    }
    
    private class CharacterState implements Serializable {
        protected int character_level;
        protected String character_class;
        protected CreatureState creature;
    }
    
    private class PlayerState implements Serializable {
        protected int score;
        protected CardPool deck;
        protected List<String> hand;
        protected List<CharacterState> party;
    }
    
    private class BoardState implements Serializable {
        protected int wave;
        protected int total_difficulty_so_far;
        protected String level_type;
        protected List<CreatureState> enemy_board;
    }
    
    
    
    private PlayerState player_state;
    private BoardState board_state;
    
    public SavedState () { }
    
    public void snapshot () {
        player_state = new PlayerState ();
        board_state = new BoardState ();
        
        player_state.score = Player.getScore();
        player_state.deck = Player.getDeck();
        
        player_state.hand = new ArrayList<> ();
        List<GuiElement> elements = CombatState.gui.getElements("card_slot");
        for (int i=0;i<elements.size();i++) {
            player_state.hand.add( elements.get(i).getCard().getId() );
        }
        
        player_state.party = new ArrayList<> ();
        List<PlayerCharacter> party = Player.getParty();
        for (int i=0;i<party.size();i++) {
            PlayerCharacter plr_char = party.get(i);
            CharacterState char_state = new CharacterState ();
            char_state.character_class = plr_char.getCharacterClass().getId();
            char_state.character_level = plr_char.getLevel();
            Creature plr_char_creature = plr_char.getCreature();
            char_state.creature = new CreatureState();
            char_state.creature.fromCreature(plr_char_creature);
        }
    }
    
}
