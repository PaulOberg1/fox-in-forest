package com.foxingarden.FoxInGarden.controller;




import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CurGameStatusMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.BaseEngineMessage;
import com.foxingarden.FoxInGarden.dto.game_alert_dtos.ServerAlertMessage;
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

    public void privateUpdate(String userId, String path, ServerAlertMessage dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
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
        String clientId = baseEngineMessage.getClientId();
        try {
            AddPlayerMessage addPlayerMessage = gameEngineService.addPlayer(baseEngineMessage);
            privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
            privateUpdate(clientId, "/p2p/playerControlUpdate");
        }
        catch (Exception e) {
            if (e.getMessage().equals("1"))
                privateUpdate(clientId, "p2p/serverAlertMessage",new ServerAlertMessage("This game already has 2 players"));
            else
                privateUpdate(clientId, "p2p/serverAlertMessage",new ServerAlertMessage("This game ID doesn't exist"));
        }
    }

    @MessageMapping("/newGame")
    public void newGame(BaseEngineMessage baseEngineMessage) throws Exception {
        AddPlayerMessage addPlayerMessage = gameEngineService.newGame(baseEngineMessage); //catch exception gameId already exists?
        String clientId = baseEngineMessage.getClientId();
        privateUpdate(clientId,"/p2p/addPlayerUpdate",addPlayerMessage);
    }
}
