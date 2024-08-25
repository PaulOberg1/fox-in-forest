package com.foxingarden.FoxInGarden.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.dto.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.PlayerDataMessage;
import com.foxingarden.FoxInGarden.dto.PlayCardMessage;
import com.foxingarden.FoxInGarden.model.domain.Game;
import com.foxingarden.FoxInGarden.model.domain.Player;

@Service
public class GameEngineService { 
    
    @Autowired
    private Game game;

    @Autowired
    private ClientPlayerMappingService clientPlayerMappingService;

    public PlayerDataMessage getPlayerData(String clientId) {
        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        return new PlayerDataMessage(clientId, player1.getScore(), player2.getScore(), player1.getId(), player2.getId());
    }

    public int playCard(PlayCardMessage playCardMessage) {
        String playerId = playCardMessage.getClientId();
        String suit = playCardMessage.getSuit();
        int rank = playCardMessage.getRank();

        game.playCard(playerId, suit, rank);
        return game.getCentralDeck().size();
    }

    public AddPlayerMessage addPlayer(String clientId) {
        Player player = game.addPlayer(clientId);
        clientPlayerMappingService.registerClient(clientId, player);
        int numPlayers = game.getPlayers().size();
        return new AddPlayerMessage(clientId,player.getDeck(),numPlayers); 
    }

    public String switchPlayerControlFrom(String prevPlayerId) {
        return game.switchPlayerControlFrom(prevPlayerId);
    }
};