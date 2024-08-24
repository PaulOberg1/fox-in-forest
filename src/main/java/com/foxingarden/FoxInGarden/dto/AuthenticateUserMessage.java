package com.foxingarden.FoxInGarden.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AuthenticateUserMessage {
    private String username;
    private String password;

    public AuthenticateUserMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
