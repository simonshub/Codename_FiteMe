/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.player;

import java.util.ArrayList;
import java.util.List;
import org.simon.src.game.data.gameplay.GameplayManager;
import org.simon.src.game.data.gameplay.cards.CardPool;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.gui.GuiElement;

/**
 *
 * @author emil.simon
 */
public class Player {
    
    private static CardPool deck;
    private static List<PlayerCharacter> party;
    
    
    
    public static void init () {
        deck = new CardPool ();
        party = new ArrayList<> ();
    }
    
    
    
    public static CardPool getDeck () {
        return deck;
    }
    
    public static List<PlayerCharacter> getParty () {
        return party;
    }
    
    public static void addCharacterToParty (PlayerCharacterClass player_character_class) {
        party.add(new PlayerCharacter (player_character_class));
    }
    
    public static void bindParty (final List<GuiElement> target_elements) {
        for (int i=0;i<target_elements.size();i++) {
            final Creature creature = party.get(i).getCreature();
            if (!creature.isDead()) {
                target_elements.get(i).setCreature(creature);
            } else {
                target_elements.get(i).setVisible(false);
            }
            GameplayManager.addAlly(creature);
        }
    }
    
}
