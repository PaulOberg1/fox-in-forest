package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Player;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Getter
public class CurGameStatusMessage extends BaseEngineMessage{

    private boolean isEnded;
    private Player curPlayer;
    private int x;

    @JsonCreator    
    public CurGameStatusMessage(@JsonProperty("clientId") String clientId,
                                @JsonProperty("gameId") String gameId,
                                @JsonProperty("isEnded") boolean isEnded,
                                @JsonProperty("curPlayer") Player curPlayer) {
        super(clientId, gameId);
        this.isEnded = isEnded;
        this.curPlayer = curPlayer;
    }
}
