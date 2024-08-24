package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class BaseMessage {
    protected String clientId;

    public BaseMessage(String clientId) {
        this.clientId = clientId;
    }
}
