package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class DetermineWinnerMessage extends BaseDTO{
    String winnerId;
    int Player1Score;
    int Player2Score;
}
