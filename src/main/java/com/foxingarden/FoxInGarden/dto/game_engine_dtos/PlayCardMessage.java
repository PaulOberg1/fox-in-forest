package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class PlayCardMessage extends BaseEngineMessage {
    private String suit;
    private int rank;

    @JsonCreator
    public PlayCardMessage(@JsonProperty("clientId") String clientId,
                           @JsonProperty("gameId") String gameId,
                           @JsonProperty("suit") String suit,
                           @JsonProperty("rank") int rank) {
        super(clientId,gameId);
        this.suit = suit;
        this.rank = rank;
    }
}
