package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class DetermineWinnerMessage extends BaseDTO {
    private String winnerId;
    private int Player1Score;
    private int Player2Score;
    private String Player1Id;
    private String Player2Id;

    public DetermineWinnerMessage(String clientId, String winnerId, int Player1Score, int Player2Score, String Player1Id, String Player2Id) {
        super(clientId);
        this.winnerId = winnerId;
        this.Player1Score = Player1Score;
        this.Player2Score = Player2Score;
        this.Player1Id = Player1Id;
        this.Player2Id = Player2Id;
    }
    public DetermineWinnerMessage(String clientId) {
        super(clientId);
        this.winnerId = "";
        this.Player1Score = -1;
        this.Player2Score = -1;
        this.Player1Id = "";
        this.Player2Id = "";
    }
}
