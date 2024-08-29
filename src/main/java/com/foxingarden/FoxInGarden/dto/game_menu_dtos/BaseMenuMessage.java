package com.foxingarden.FoxInGarden.dto.game_menu_dtos;

import lombok.Setter;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
public class BaseMenuMessage {
    protected String clientId;

    @JsonCreator
    public BaseMenuMessage(@JsonProperty("clientId") String clientId) {
        this.clientId = clientId;
    }
}
