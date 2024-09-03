package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class CentralDeckMessage extends BaseEngineMessage {
    protected List<String> playerIds;
    protected List<String> cardSuits;
    protected List<Integer> cardRanks;
    protected String winner;
    protected Boolean endRound;

    @JsonCreator
    public CentralDeckMessage(@JsonProperty("clientId") String clientId, 
                              @JsonProperty("gameId") String gameId,
                              @JsonProperty("playerIds") List<String> playerIds,
                              @JsonProperty("cardSuits") List<String> cardSuits,
                              @JsonProperty("cardRanks") List<Integer> cardRanks,
                              @JsonProperty("winner") String winner,
                              @JsonProperty("endRound") boolean endRound) {
        super(clientId,gameId);
        this.playerIds = playerIds;
        this.cardSuits = cardSuits;
        this.cardRanks = cardRanks;
        this.winner = winner;
        this.endRound = endRound;
    }
}
