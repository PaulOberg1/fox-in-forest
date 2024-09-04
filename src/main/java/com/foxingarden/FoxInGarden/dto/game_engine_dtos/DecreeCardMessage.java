package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

@Getter
public class DecreeCardMessage extends BaseEngineMessage {
    private String suit;
    private int rank;

    public DecreeCardMessage(String clientId, String gameId, String suit, int rank) {
        super(clientId,gameId);
        this.suit = suit;
        this.rank = rank;
    }
}
