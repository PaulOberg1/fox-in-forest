package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class DetermineWinnerMessage extends BaseDTO{
    private String winnerId;
    private int Player1Score;
    private int Player2Score;

    public DetermineWinnerMessage(String winnerId, int Player1Score, int Player2Score) {
        this.winnerId = winnerId;
        this.Player1Score = Player1Score;
        this.Player2Score = Player2Score;
    }
}
