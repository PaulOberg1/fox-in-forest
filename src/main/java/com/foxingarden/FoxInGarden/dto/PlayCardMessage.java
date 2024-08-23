package com.foxingarden.FoxInGarden.dto;

import lombok.Getter;

@Getter
public class PlayCardMessage extends BaseDTO {
    private String playerId;
    private String suit;
    private int rank;
}
