package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Player;

import lombok.Getter;

@Getter
public class CurGameStatusMessage extends BaseEngineMessage{

    private boolean isEnded;
    private Player curPlayer;
    private int x;
    
    public CurGameStatusMessage(String clientId, String gameId, boolean isEnded, Player curPlayer) {
        super(clientId, gameId);
        this.isEnded = isEnded;
        this.curPlayer = curPlayer;
    }
}
