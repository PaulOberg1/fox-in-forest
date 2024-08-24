package com.foxingarden.FoxInGarden.dto;

import com.foxingarden.FoxInGarden.model.domain.Deck;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage extends BaseMessage {
    private Deck deck;
    private int numPlayers;

    public AddPlayerMessage(String clientId, Deck deck, int numPlayers) {
        super(clientId);
        this.deck = deck;
        this.numPlayers = numPlayers;
    }
}
