package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import lombok.Getter;

@Getter
public class EndGameMessage extends BaseEngineMessage {
    private String player1Id;
    private String player2Id;
    
    private int player1Points;
    private int player2Points;

    public EndGameMessage(String clientId, String gameId, String player1Id, String player2Id, int player1Points, int player2Points) {
        super(clientId, gameId);
        this.player1Id = player1Id;
        this.player2Id = player2Id;

        this.player1Points = player1Points;
        this.player2Points = player2Points;
    }
}
