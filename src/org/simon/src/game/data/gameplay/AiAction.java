/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.gameplay;

import java.util.List;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.gui.GuiElement;
import org.simon.src.game.sfx.SpecialEffectSystem;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author XyRoN
 */
public class AiAction {
    
    private boolean started=false;
    private boolean finished=false;
    
    private final Card card;
    private final Creature source;
    private final List<Creature> targets;
    private final SpecialEffectSystem sfx;
    
    public AiAction (final Card card, final Creature source, final List<Creature> targets, final SpecialEffectSystem sfx) {
        this.card = card;
        this.source = source;
        this.targets = targets;
        this.sfx = sfx;
    }
    
    public void start () {
        started = true;
        card.play(sfx, source, targets);
        
        GuiElement enemy_played_card = CombatState.gui.getElement("enemy_played_card");
        enemy_played_card.setCard(card);
        enemy_played_card.setVisible(true);
    }
    
    public void update () {
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
