package com.foxingarden.FoxInGarden.controller;




import com.foxingarden.FoxInGarden.dto.BaseDTO;
import com.foxingarden.FoxInGarden.dto.DetermineWinnerMessage;
import com.foxingarden.FoxInGarden.dto.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.AddPlayerMessage;

import com.foxingarden.FoxInGarden.model.domain.Deck;

import com.foxingarden.FoxInGarden.service.GameEngineService;



import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;



@RestController
class GameEngineController{

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastUpdate(String path, BaseDTO dto) {
        messagingTemplate.convertAndSend(path, dto);
    }

    public void privateUpdate(String userId, String path, BaseDTO dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, null);
    }

    @MessageMapping("/playCard")
    public void playCard(PlayCardMessage playCardMessage) {
        int centralDeckSize = gameEngineService.playCard(playCardMessage);
        broadcastUpdate("/broadcast/updateCentralDeck", playCardMessage);
        if (centralDeckSize == 2) {
            DetermineWinnerMessage determineWinnerMessage = gameEngineService.determineWinner();
            broadcastUpdate("broadcast/determineWinnerUpdate", determineWinnerMessage);
            String playerId = determineWinnerMessage.getWinnerId();
            privateUpdate(playerId, "/p2p/transferPlayerUpdate");
        }
        else {
            String nextPlayerId = gameEngineService.getOtherPlayerId(playCardMessage.getPlayerId());
            privateUpdate(nextPlayerId,"/p2p/transferPlayerUpdate");
        }
    }

    @MessageMapping("/addPlayer")
    public AddPlayerMessage addPlayer(String playerId) {
        return gameEngineService.addPlayer(playerId); 
    }
}