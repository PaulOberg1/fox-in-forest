package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;

import lombok.Getter;

@Setter
@Getter
public class PlayerDataMessage extends BaseMessage {
    private int player1Score;
    private int player2Score;

    private String player1ClientId;
    private String player2ClientId;

    public PlayerDataMessage(String clientId, int player1Score, int player2Score, String player1ClientId, String player2ClientId) {
        super(clientId);
        this.player1Score = player1Score;
        this.player2Score = player2Score;

        this.player1ClientId = player1ClientId;
        this.player2ClientId = player2ClientId;
    }
}
