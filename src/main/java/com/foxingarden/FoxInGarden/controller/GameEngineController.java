package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import com.foxingarden.FoxInGarden.dto.BaseDTO;
import com.foxingarden.FoxInGarden.dto.DetermineWinnerMessage;
import com.foxingarden.FoxInGarden.dto.PlayCardMessage;
import com.foxingarden.FoxInGarden.model.domain.Deck;
import com.foxingarden.FoxInGarden.service.GameEngineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;



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

    @MessageMapping("/playCard")
    public void playCard(PlayCardMessage playCardMessage) {
        Deck centralDeck = gameEngineService.playCard(playCardMessage); //playCardMessage contains user id
        broadcastUpdate("/broadcast/updateCentralDeck", playCardMessage); //send Central Deck instead of playcardmsg
        if (centralDeck.length() == 2) {
            // Send central deck to service layer, return DTO containing updated scores
            DetermineWinnerMessage determineWinnerMessage = gameEngineService.determineWinner(centralDeck);
            broadcastUpdate("broadcast/determineWinnerUpdate", determineWinnerMessage);
            String playerId = determineWinnerMessage.getWinnerId();
            privateUpdate(playerId, "/p2p/selectWinnerUpdate", determineWinnerMessage);
        }
    }
}