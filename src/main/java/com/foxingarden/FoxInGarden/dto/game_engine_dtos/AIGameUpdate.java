package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Card;
import com.foxingarden.FoxInGarden.model.domain.Deck;

import lombok.Getter;

@Getter
public class AIGameUpdate extends BaseEngineMessage {
    Deck playerDeck;
    Deck opDeck;
    Card decreeCard;

    public AIGameUpdate(String clientId, String gameId, Deck playerDeck, Deck opDeck, Card decreeCard) {
        super(clientId, gameId);
        this.playerDeck = playerDeck;
        this.opDeck = opDeck;
        this.decreeCard = decreeCard;
    }
}
