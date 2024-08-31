package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Setter;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Setter
@Getter
public class BaseEngineMessage {
    protected String clientId;
    protected String gameId;

    @JsonCreator
    public BaseEngineMessage(@JsonProperty("clientId") String clientId,
                             @JsonProperty("gameId") String gameId) {
        this.clientId = clientId;
        this.gameId = gameId;
    }
}
