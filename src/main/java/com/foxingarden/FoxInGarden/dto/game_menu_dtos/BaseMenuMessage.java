package com.foxingarden.FoxInGarden.dto.game_menu_dtos;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class BaseMenuMessage {
    protected String clientId;

    public BaseMenuMessage(String clientId) {
        this.clientId = clientId;
    }
}
