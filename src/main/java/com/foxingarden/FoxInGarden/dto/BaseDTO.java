package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public abstract class BaseDTO {
    protected String clientId;

    public BaseDTO(String clientId) {
        this.clientId = clientId;
    }
}
