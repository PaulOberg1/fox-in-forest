package com.foxingarden.FoxInGarden.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CentralDeckMessage extends BaseMessage {
    private List<String> playerIds;
    private List<String> cardSuits;
    private List<Integer> cardRanks;

    public CentralDeckMessage(String clientId, List<String> playerIds, List<String> cardSuits, List<Integer> cardRanks) {
        super(clientId);
        this.playerIds = playerIds;
        this.cardSuits = cardSuits;
        this.cardRanks = cardRanks;
    }
}
