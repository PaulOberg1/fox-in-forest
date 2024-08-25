package com.foxingarden.FoxInGarden.controller;




import com.foxingarden.FoxInGarden.dto.BaseMessage;
import com.foxingarden.FoxInGarden.dto.PlayerDataMessage;
import com.foxingarden.FoxInGarden.dto.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.AddPlayerMessage;

import com.foxingarden.FoxInGarden.service.GameEngineService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;



@RestController
class GameEngineController{

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastUpdate(String path, BaseMessage dto) {
        messagingTemplate.convertAndSend(path, dto);
    }

    public void privateUpdate(String userId, String path, BaseMessage dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, null);
    }

    @MessageMapping("/playCard")
    public void playCard(PlayCardMessage playCardMessage) {
        String clientId = playCardMessage.getClientId();
        int centralDeckSize = gameEngineService.playCard(playCardMessage);
        broadcastUpdate("/broadcast/centralDeckUpdate", playCardMessage);
        if (centralDeckSize == 2) {
            PlayerDataMessage playerDataMessage = gameEngineService.getPlayerData(clientId);
            broadcastUpdate("broadcast/updateScores", playerDataMessage);
        }
        else {
            String nextClientId = gameEngineService.switchPlayerControlFrom(playCardMessage.getClientId());
            privateUpdate(nextClientId,"/p2p/transferPlayerUpdate");
        }
    }

    @MessageMapping("/addPlayer")
    public void addPlayer(BaseMessage baseMessage) {
        String clientId = baseMessage.getClientId();
        AddPlayerMessage addPlayerMessage = gameEngineService.addPlayer(clientId);
        privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
        if (addPlayerMessage.getNumPlayers()==2) {
            privateUpdate(clientId, "/p2p/transferPlayerUpdate");
        }
    }
}