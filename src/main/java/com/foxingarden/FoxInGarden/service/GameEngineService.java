package com.foxingarden.FoxInGarden.service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AIGameUpdate;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.BaseEngineMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayCardMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.PlayerDataMessage;
import com.foxingarden.FoxInGarden.mcts.Action;
import com.foxingarden.FoxInGarden.mcts.MCTS;
import com.foxingarden.FoxInGarden.mcts.Node;
import com.foxingarden.FoxInGarden.mcts.State;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CurGameStatusMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;
import com.foxingarden.FoxInGarden.model.domain.GameSession;
import com.foxingarden.FoxInGarden.model.domain.Card;
import com.foxingarden.FoxInGarden.model.domain.Deck;
import com.foxingarden.FoxInGarden.model.domain.Game;
import com.foxingarden.FoxInGarden.model.domain.Player;

@Service
public class GameEngineService { 
    
    private final ConcurrentHashMap<String,GameSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ClientPlayerMappingService clientPlayerMappingService;

    private MCTS mcts;

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

        CentralDeckMessage centralDeckMessage = game.getCentralDeckState(clientId);
        if (centralDeckMessage.getEndRound()) {
            game.setCurPlayer(gameSession.getPlayerById(centralDeckMessage.getWinner()));
        }
        else {
            Player nextPlayer = gameSession.getOtherPlayerById(clientId);
            game.setCurPlayer(nextPlayer);
        }
        return centralDeckMessage;
    }

    public String getOtherPlayerId(String clientId, String gameId) {
        GameSession gameSession = sessions.get(gameId);
        return gameSession.getOtherPlayerById(clientId).getId();
    }

    public AddPlayerMessage addPlayer(BaseEngineMessage baseEngineMessage) throws Exception {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();

        gameSession.addPlayer(clientId);
        Player player = gameSession.getPlayerById(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        game.setCurPlayer(player);
        return new AddPlayerMessage(clientId,gameId,player.getDeck().getCards()); 
    }

    public AddPlayerMessage newGame(BaseEngineMessage baseEngineMessage) throws Exception {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = new GameSession(gameId);
        sessions.put(gameId,gameSession);

        gameSession.addPlayer(clientId);
        Player player = gameSession.getPlayerById(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        return new AddPlayerMessage(clientId,gameId,player.getDeck().getCards());
    }

    public Card setDecreeCard(String gameId) {
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();
        return game.extractDecreeCard();
    }

    public CurGameStatusMessage getCurGameStatus(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();

        ArrayList<String> userIds = new ArrayList<>();
        ArrayList<Integer> numPlayerCards = new ArrayList<>();
        for (Player player: gameSession.getPlayers()) {
            userIds.add(player.getId());
            numPlayerCards.add(player.getDeck().length());
        }

        return new CurGameStatusMessage(clientId, gameId, game.isEnded(gameSession.getPlayers()), game.getCurPlayer(), userIds, numPlayerCards);
    }

    public EndGameMessage endGame(BaseEngineMessage baseEngineMessage) {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();
        
        return game.endGame(clientId,gameSession.getPlayers());
    }

    public AIGameUpdate newAIGame(BaseEngineMessage baseEngineMessage) throws Exception {
        String clientId = baseEngineMessage.getClientId();
        String gameId = baseEngineMessage.getGameId();
        GameSession gameSession = new GameSession(gameId);
        Game game = gameSession.getGame();
        sessions.put(gameId,gameSession);

        gameSession.addPlayer(clientId);
        Player player = gameSession.getPlayerById(clientId);
        clientPlayerMappingService.registerClient(clientId, player);

        Deck playerDeck = player.getDeck();
        Deck opDeck = game.extractRandomDeck();
        Card decreeCard = game.extractDecreeCard();

        State initialState = new State(playerDeck,opDeck,decreeCard);
        Node rootNode = new Node(initialState,null,true);
        mcts = new MCTS(rootNode);
        
        return new AIGameUpdate(clientId,gameId,playerDeck,opDeck,decreeCard);
    }

    public AIGameUpdate playCardAgainstAI(PlayCardMessage playCardMessage) {
        String clientId = playCardMessage.getClientId();
        String gameId = playCardMessage.getGameId();
        String suit = playCardMessage.getSuit();
        int rank = playCardMessage.getRank();

        GameSession gameSession = sessions.get(gameId);
        Game game = gameSession.getGame();
        Player player = gameSession.getPlayerById(clientId);
        game.playCard(player, suit, rank);

        Action playerAction = new Action(new Card(suit,rank), true);
        mcts.updateRoot(playerAction);

        Deck playerDeck = mcts.getRoot().getState().getPlayerDeck();
        Deck opDeck = mcts.getRoot().getState().getOpDeck();
        Card decreeCard = mcts.getRoot().getState().getDecreeCard();

        return new AIGameUpdate(clientId,gameId,playerDeck,opDeck,decreeCard);
        
    }

    public AIGameUpdate playAICardAgainstPlayer(AIGameUpdate aIGameUpdate) {
        String clientId = aIGameUpdate.getClientId();
        String gameId = aIGameUpdate.getGameId();

        mcts.search(1000);
        Action bestAction = mcts.getBestAction();
        mcts.updateRoot(bestAction);

        Deck playerDeck = mcts.getRoot().getState().getPlayerDeck();
        Deck opDeck = mcts.getRoot().getState().getOpDeck();
        Card decreeCard = mcts.getRoot().getState().getDecreeCard();

        return new AIGameUpdate(clientId,gameId,playerDeck,opDeck,decreeCard);


    }
};
