package com.foxingarden.FoxInGarden.dto;

import lombok.Getter;

@Getter
public class PlayCardMessage extends BaseMessage {
    private String suit;
    private int rank;

    public PlayCardMessage(String clientId, String suit, int rank) {
        super(clientId);
        this.suit = suit;
        this.rank = rank;
    }
}
