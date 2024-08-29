package com.foxingarden.FoxInGarden.mcts;

import com.foxingarden.FoxInGarden.model.domain.Card;

import lombok.Getter;

@Getter
public class Action {
    private Card playerCard;
    private Card opCard;

    public Action(Card playerCard, Card opCard) {
        this.playerCard = playerCard;
        this.opCard = opCard;
    }

}
