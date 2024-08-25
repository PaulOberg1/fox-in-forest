package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class CentralDeckMessage extends BaseEngineMessage {
    private List<String> playerIds;
    private List<String> cardSuits;
    private List<Integer> cardRanks;

    public CentralDeckMessage(String clientId, String gameId, List<String> playerIds, List<String> cardSuits, List<Integer> cardRanks) {
        super(clientId,gameId);
        this.playerIds = playerIds;
        this.cardSuits = cardSuits;
        this.cardRanks = cardRanks;
    }
}
