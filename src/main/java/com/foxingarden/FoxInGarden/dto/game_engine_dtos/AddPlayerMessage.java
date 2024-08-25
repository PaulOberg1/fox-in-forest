package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Deck;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage extends BaseEngineMessage {
    private Deck deck;
    private int numPlayers;

    public AddPlayerMessage(String clientId, String gameId, Deck deck, int numPlayers) {
        super(clientId,gameId);
        this.deck = deck;
        this.numPlayers = numPlayers;
    }
}
