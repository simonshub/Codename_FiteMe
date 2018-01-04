/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.sfx;

import java.util.ArrayList;
import java.util.List;
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
    private final List<Creature> targets;
    private final List<CardAction> actions;
    
    
    
    public SpecialEffectCallback (Card parent, Creature source, List<Creature> targets, List<CardAction> actions) {
        this.parent = parent;
        this.source = source;
        this.targets = targets;
        this.actions = actions;
    }
    
    
    
    public void call () {
        for (CardAction action : actions) {
            for (Creature target : targets)
                action.call(source, parent, target);
        }
    }
    
    public Card getParent () {
        return parent;
    }
    
    public Creature getSource () {
        return source;
    }
    
    public List<Creature> getTargets () {
        return targets;
    }
    
    public List<CardAction> getActions () {
        return actions;
    }
    
    @Override
    public String toString () {
        List<String> id_list = new ArrayList<> ();
        for (Creature target : targets)
            id_list.add(target.getId());
        
        String contents = "";
        contents += " { ";
        contents += " [ "+"parent:"+parent.getId()+" ] ";
        contents += " [ "+"source:"+source.getId()+" ] ";
        contents += " [ "+"targets:"+SlickUtils.getListAsStringList(id_list, ", ")+" ] ";
        contents += " [ "+"actions:"+SlickUtils.getListAsStringList(actions, ", ")+" ] ";
        contents += " } ";
        return contents;
    }
}
