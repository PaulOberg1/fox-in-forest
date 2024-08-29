package com.foxingarden.FoxInGarden.dto.game_menu_dtos;

import lombok.Setter;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@Setter
@Getter
public class AuthenticateUserMessage extends BaseMenuMessage {
    private String username;
    private String password;

    @JsonCreator
    public AuthenticateUserMessage(@JsonProperty("clientId") String clientId,
                                   @JsonProperty("username") String username,
                                   @JsonProperty("password") String password) {
        super(clientId);
        this.username = username;
        this.password = password;
    }
}
