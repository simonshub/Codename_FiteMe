/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.util.List;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.sfx.SpecialEffectSystem;

/**
 *
 * @author XyRoN
 */
public class AiAction {
    
    private boolean started=false;
    private boolean finished=false;
    
    private Card card;
    private Creature source;
    private List<Creature> targets;
    private SpecialEffectSystem sfx;
    
    public AiAction (final Card card, final Creature source, final List<Creature> targets, final SpecialEffectSystem sfx) {
        this.card = card;
        this.source = source;
        this.targets = targets;
        this.sfx = sfx;
    }
    
    public void start () {
        started = true;
        card.play(sfx, source, targets);
    }
    
    public void update (SpecialEffectSystem sfx) {
        if (finished) return;
        
        if (!started) start();
        else if (sfx.isEmpty()) finished = true;
    }
    
    public boolean isStarted () {
        return started;
    }
    
    public boolean isFinished () {
        return finished;
    }
}
