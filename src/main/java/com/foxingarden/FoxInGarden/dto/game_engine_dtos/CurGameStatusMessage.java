package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Player;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;


@Getter
public class CurGameStatusMessage extends BaseEngineMessage{

    private boolean isEnded;
    private Player curPlayer;
    private int x;
    private ArrayList<String> userIds;
    private ArrayList<Integer> numPlayerCards;

    @JsonCreator    
    public CurGameStatusMessage(@JsonProperty("clientId") String clientId,
                                @JsonProperty("gameId") String gameId,
                                @JsonProperty("isEnded") boolean isEnded,
                                @JsonProperty("curPlayer") Player curPlayer,
                                @JsonProperty("userIds") ArrayList<String> userIds,
                                @JsonProperty("numPlayerCards") ArrayList<Integer> numPlayerCards) {
        super(clientId, gameId);
        this.isEnded = isEnded;
        this.curPlayer = curPlayer;
        this.userIds = userIds;
        this.numPlayerCards = numPlayerCards;
    }
}
