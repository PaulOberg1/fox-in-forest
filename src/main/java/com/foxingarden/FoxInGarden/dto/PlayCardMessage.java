package com.foxingarden.FoxInGarden.dto;

import lombok.Getter;

@Getter
public class PlayCardMessage extends BaseDTO {
    private String playerId;
    private String suit;
    private int rank;

    public PlayCardMessage(String clientId, String playerId, String suit, int rank) {
        super(clientId);
        this.playerId = playerId;
        this.suit = suit;
        this.rank = rank;
    }
}
