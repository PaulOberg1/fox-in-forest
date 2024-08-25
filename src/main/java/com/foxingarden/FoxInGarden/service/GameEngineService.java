package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.BaseEngineMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayerDataMessage;
import com.foxingarden.FoxInGarden.model.domain.Game;
import com.foxingarden.FoxInGarden.model.domain.Player;

@Service
public class GameEngineService { 
    
    private final ConcurrentHashMap<String,Game> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ClientPlayerMappingService clientPlayerMappingService;

    public PlayerDataMessage getPlayerData(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        Game game = sessions.get(gameId);

        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        return new PlayerDataMessage(clientId, gameId, player1.getScore(), player2.getScore(), player1.getId(), player2.getId());
    }

    public CentralDeckMessage playCard(PlayCardMessage playCardMessage) {
        String clientId = playCardMessage.getClientId();
        String gameId = playCardMessage.getGameId();
        String suit = playCardMessage.getSuit();
        int rank = playCardMessage.getRank();

        Game game = sessions.get(gameId);

        game.playCard(clientId, suit, rank);

        return game.getCentralDeckState(clientId);
    }

    public AddPlayerMessage addPlayer(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        Game game = sessions.get(gameId);

        Player player = game.addPlayer(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        int numPlayers = game.getPlayers().size();
        return new AddPlayerMessage(clientId,gameId,player.getDeck(),numPlayers); 
    }

    public String switchPlayerControl(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        Game game = sessions.get(gameId);
        
        return game.switchPlayerControlFrom(clientId);
    }
};