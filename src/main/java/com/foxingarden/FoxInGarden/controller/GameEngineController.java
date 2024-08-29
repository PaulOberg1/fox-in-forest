package com.foxingarden.FoxInGarden.controller;




import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CurGameStatusMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.BaseEngineMessage;
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

    public void broadcastUpdate(String path, BaseEngineMessage dto) {
        messagingTemplate.convertAndSend(path, dto);
    }

    public void privateUpdate(String userId, String path, BaseEngineMessage dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, "{}");
    }

    @MessageMapping("/playCard")
    public void playCard(PlayCardMessage playCardMessage) {
        String clientId = playCardMessage.getClientId();
        String gameId = playCardMessage.getGameId();
        CentralDeckMessage centralDeckMessage = gameEngineService.playCard(playCardMessage);
        broadcastUpdate("/broadcast/{game_id}/centralDeckUpdate", centralDeckMessage);

        CurGameStatusMessage curGameStatusMessage = gameEngineService.getCurGameStatus(new BaseEngineMessage(clientId, gameId));

        if (curGameStatusMessage.isEnded()) {
            EndGameMessage endGameMessage = gameEngineService.endGame(new BaseEngineMessage(clientId, gameId));
            broadcastUpdate("/{game_id}/endGame", endGameMessage);
        }
        else {
            String newClientId = curGameStatusMessage.getCurPlayer().getId();
            privateUpdate("/p2p/playerControlUpdate", newClientId, new BaseEngineMessage(newClientId, gameId));
        }
    }

    @MessageMapping("/addPlayer")
    public void addPlayer(BaseEngineMessage baseEngineMessage) {
        AddPlayerMessage addPlayerMessage = gameEngineService.addPlayer(baseEngineMessage);
        String clientId = baseEngineMessage.getClientId();
        privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
        if (addPlayerMessage.getNumPlayers()==2) {
            privateUpdate(clientId, "/p2p/transferPlayerUpdate");
        }
    }
}
