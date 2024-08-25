package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

@Getter
public class PlayCardMessage extends BaseEngineMessage {
    private String suit;
    private int rank;

    public PlayCardMessage(String clientId, String gameId, String suit, int rank) {
        super(clientId,gameId);
        this.suit = suit;
        this.rank = rank;
    }
}
