package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AuthenticateUserMessage extends BaseMessage {
    private String username;
    private String password;

    public AuthenticateUserMessage(String clientId, String username, String password) {
        super(clientId);
        this.username = username;
        this.password = password;
    }
}
