package com.foxingarden.FoxInGarden.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayCardMessage {
    private String playerId;
    private String suit;
    private int rank;


}
