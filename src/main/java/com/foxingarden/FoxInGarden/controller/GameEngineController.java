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
        broadcastUpdate("/broadcast/" + gameId + "/centralDeckUpdate", centralDeckMessage);
        
        CurGameStatusMessage curGameStatusMessage = gameEngineService.getCurGameStatus(new BaseEngineMessage(clientId, gameId)); //if this message is not sent to the client maybe it would be more efficient to return something else as the curGameStatus
        broadcastUpdate("/broadcast/" + gameId + "/gameStatusUpdate", curGameStatusMessage);

        if (curGameStatusMessage.isEnded()) {
            EndGameMessage endGameMessage = gameEngineService.endGame(new BaseEngineMessage(clientId, gameId));
            broadcastUpdate("/broadcast/" + gameId + "/endGame", endGameMessage);
        }
        else {
            String newClientId = curGameStatusMessage.getCurPlayer().getId();
            privateUpdate(newClientId, "/p2p/playerControlUpdate");
        }
    }

    @MessageMapping("/addPlayer")
    public void addPlayer(BaseEngineMessage baseEngineMessage) {
        AddPlayerMessage addPlayerMessage = gameEngineService.addPlayer(baseEngineMessage); //catch exception gameId does not exist
        String clientId = baseEngineMessage.getClientId();
        privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
        privateUpdate(clientId, "/p2p/playerControlUpdate");
    }

    @MessageMapping("/newGame")
    public void newGame(BaseEngineMessage baseEngineMessage) {
        AddPlayerMessage addPlayerMessage = gameEngineService.newGame(baseEngineMessage); //catch exception gameId already exists?
        String clientId = baseEngineMessage.getClientId();
        privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
    }
}
