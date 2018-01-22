/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.player.Player;
import org.simon.src.game.data.gameplay.player.PlayerCharacter;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author emil.simon
 */
public class PlayerState implements Serializable {
    protected int score;
    protected List<String> hand;
    protected List<Boolean> played_hand;
    protected List<CharacterState> party;
    
    protected void apply () {
        Player.setScore(score);
        
        List<GuiElement> card_elements = CombatState.gui.getElements("card_slot");
        for (int i=0;i<card_elements.size();i++) {
            Card card = CardLibrary.getCard(hand.get(i));
            card_elements.get(i).setCard(card);
            card_elements.get(i).setCardPlayed(played_hand.get(i));
        }
        
        Player.clearParty();
        for (int i=0;i<party.size();i++) {
            PlayerCharacter pc = party.get(i).asPlayerCharacter();
            Player.addCharacterToParty(pc);
        }
        
        List<GuiElement> board_elements = CombatState.gui.getElements("_ally_");
        Player.bindParty(board_elements);
        
        
        for (int i=0;i<party.size();i++) {
            party.get(i).creature.applyToCreature(Player.getParty().get(i).getCreature());
        }
    }
    
    protected void applyStatusEffects () {
        for (int i=0;i<party.size();i++) {
            party.get(i).creature.applyStatusEffects();
        }
    }
}