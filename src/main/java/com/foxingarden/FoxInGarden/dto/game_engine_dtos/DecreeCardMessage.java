package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

@Getter
public class DecreeCardMessage extends BaseEngineMessage {
    private String cardSuit;
    private int cardRank;

    public DecreeCardMessage(String clientId, String gameId, String cardSuit, int cardRank) {
        super(clientId,gameId);
        this.cardSuit = cardSuit;
        this.cardRank = cardRank;
    }
}
