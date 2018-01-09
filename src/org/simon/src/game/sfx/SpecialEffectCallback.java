/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.util.List;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardAction;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.utils.SlickUtils;

/**
 *
 * @author XyRoN
 */
public class SpecialEffectCallback {
    private final Card parent;
    private final Creature source;
    private final Creature target;
    private final List<CardAction> actions;
    
    
    
    public SpecialEffectCallback (Card parent, Creature source, Creature target) {
        this.parent = parent;
        this.source = source;
        this.target = target;
        this.actions = parent.getActions();
    }
    
    public SpecialEffectCallback (StatusEffect parent, Creature source, Creature target) {
        this.parent = parent.getParent();
        this.source = source;
        this.target = target;
        this.actions = parent.getActions();
    }
    
    
    
    public void call () {
        for (CardAction action : actions) {
            action.call(source, parent, target);
        }
    }
    
    public Card getParent () {
        return parent;
    }
    
    public Creature getSource () {
        return source;
    }
    
    public Creature getTarget () {
        return target;
    }
    
    public List<CardAction> getActions () {
        return actions;
    }
    
    @Override
    public String toString () {
        String contents = "";
        contents += " { ";
        contents += " [ "+"parent:"+parent.getId()+" ] ";
        contents += " [ "+"source:"+source.getId()+" ] ";
        contents += " [ "+"target:"+target.getId()+" ] ";
        contents += " [ "+"actions:"+SlickUtils.getListAsStringList(actions, ", ")+" ] ";
        contents += " } ";
        return contents;
    }
}
