package com.foxingarden.FoxInGarden.mcts;

import com.foxingarden.FoxInGarden.model.domain.Card;

import lombok.Getter;

@Getter
public class Action {
    private Card card;
    private boolean isPlayer;

    public Action(Card card, boolean isPlayer) {
        this.card = card;
        this.isPlayer = isPlayer;
    }

}
