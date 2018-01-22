/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simon.src.game.data.gameplay.player.PlayerCharacterClass;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class CardPool implements Serializable {
    
    private final Map<String, Integer> cards;
    
    
    
    public CardPool () {
        cards = new HashMap<> ();
    }
    
    public CardPool (List<Card> card_list) {
        cards = new HashMap<> ();
        
        for (Card card : card_list)
            addCard(card);
    }
    
    
    
    public void addCard (String name) {
        if (CardLibrary.getCard(name)==null) return;
        
        if (cards.containsKey(name)) {
            int count = cards.get(name) + 1;
            cards.put(name, count);
        } else {
            cards.put(name, 1);
        }
    }
    
    public final void addCard (Card card) {
        addCard(card.getId());
    }
    
    public final void addAllCards (PlayerCharacterClass char_class, int lvl) {
        char_class.getCardListForLevel(lvl).forEach((card) -> {
            addCard(card);
        });
    }
    
    public final void clear () {
        cards.clear();
    }
    
    
    
    public List<Card> getCardList () {
        List<Card> all_cards = new ArrayList<> ();
        
        for (String key : cards.keySet()) {
            int c = cards.get(key);
            for (int i=0;i<c;i++) {
                all_cards.add(CardLibrary.getCard(key));
            }
        }
        
        return all_cards;
    }
    
    public List<Card> getRandomCards (int count) {
        if (cards.isEmpty())
            return new ArrayList<> ();
        
        List<Card> hand = new ArrayList<> ();
        List<Card> all_cards = getCardList();
        
        for (int i=0;i<count;i++) {
            int index = SlickUtils.randIndex(all_cards.size());
            hand.add(all_cards.get(index));
            all_cards.remove(index);
        }
        
        return hand;
    }
    
}
