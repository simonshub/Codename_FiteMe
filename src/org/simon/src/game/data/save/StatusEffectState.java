/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simon.src.game.data.save;

import java.io.Serializable;
import org.simon.src.game.data.gameplay.StatusEffect;
import org.simon.src.game.data.gameplay.cards.Card;
import org.simon.src.game.data.gameplay.cards.CardLibrary;
import org.simon.src.game.data.gameplay.creatures.Creature;
import org.simon.src.game.states.combat.CombatState;

/**
 *
 * @author emil.simon
 */
public class StatusEffectState implements Serializable {
    protected int counter;
    protected String which_card;
    protected String caster_gui_element;
    protected String target_gui_element;
    protected String display_string;
    protected String action_string;

    protected void fromStatusEffect (final StatusEffect status_effect) {
        this.counter = status_effect.getCounter();
        this.which_card = status_effect.getParent().getId();
        this.caster_gui_element = status_effect.getSource().getGuiElement().getName();
        this.target_gui_element = status_effect.getTarget().getGuiElement().getName();
        this.display_string = status_effect.getOriginalDisplayString();
        this.action_string = status_effect.getOriginalActionString();
    }

    protected StatusEffect apply () {
        StatusEffect status_effect = null;

        Card card = CardLibrary.getCard(which_card);
        Creature caster = CombatState.gui.getElement(caster_gui_element).getCreature();
        Creature target = CombatState.gui.getElement(target_gui_element).getCreature();

        status_effect = new StatusEffect(card, caster, target, counter, action_string, display_string);
        target.addStatusEffect(status_effect);

        return status_effect;
    }
}
