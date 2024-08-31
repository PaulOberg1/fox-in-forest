package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.BaseEngineMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayerDataMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CurGameStatusMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;
import com.foxingarden.FoxInGarden.model.domain.GameSession;
import com.foxingarden.FoxInGarden.model.domain.Game;
import com.foxingarden.FoxInGarden.model.domain.Player;

@Service
public class GameEngineService { 
    
    private final ConcurrentHashMap<String,GameSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ClientPlayerMappingService clientPlayerMappingService;

    public PlayerDataMessage getPlayerData(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Player player1 = gameSession.getPlayers().get(0);
        Player player2 = gameSession.getPlayers().get(1);
        return new PlayerDataMessage(clientId, gameId, player1.getScore(), player2.getScore(), player1.getId(), player2.getId());
    }

    public CentralDeckMessage playCard(PlayCardMessage playCardMessage) {
        String clientId = playCardMessage.getClientId();
        String gameId = playCardMessage.getGameId();
        String suit = playCardMessage.getSuit();
        int rank = playCardMessage.getRank();

        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();
        Player player = gameSession.getPlayerById(clientId);
        game.playCard(player, suit, rank);

        Player nextPlayer = gameSession.getOtherPlayerById(clientId);
        game.setCurPlayer(nextPlayer);

        return game.getCentralDeckState(clientId);
    }

    public AddPlayerMessage addPlayer(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();

        gameSession.addPlayer(clientId);
        Player player = gameSession.getPlayerById(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        game.setCurPlayer(player);
        int numPlayers = gameSession.getPlayers().size();
        return new AddPlayerMessage(clientId,gameId,player.getDeck().getCards(),numPlayers); 
    }

    public AddPlayerMessage newGame(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = new GameSession(gameId);
        sessions.put(gameId,gameSession);

        gameSession.addPlayer(clientId);
        Player player = gameSession.getPlayerById(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        return new AddPlayerMessage(clientId,gameId,player.getDeck().getCards(),1);
    }

    public CurGameStatusMessage getCurGameStatus(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();

        return new CurGameStatusMessage(clientId, gameId, game.isEnded(gameSession.getPlayers()), game.getCurPlayer());
    }

    public EndGameMessage endGame(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();
        
        return game.endGame(clientId,gameSession.getPlayers());


    }
};
