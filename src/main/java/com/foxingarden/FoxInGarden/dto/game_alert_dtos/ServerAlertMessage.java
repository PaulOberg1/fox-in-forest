package com.foxingarden.FoxInGarden.dto.game_alert_dtos;

import lombok.Getter;


@Getter
public class ServerAlertMessage {

    private String serverMessage;

    public ServerAlertMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }
}
