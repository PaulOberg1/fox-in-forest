package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class BaseEngineMessage {
    protected String clientId;
    protected String gameId;

    public BaseEngineMessage(String clientId, String gameId) {
        this.clientId = clientId;
        this.gameId = gameId;
    }
}
