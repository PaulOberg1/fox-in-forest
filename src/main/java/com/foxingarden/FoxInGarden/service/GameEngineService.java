package com.foxingarden.FoxInGarden.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.dto.AddPlayerMessage;
import com.foxingarden.FoxInGarden.dto.DetermineWinnerMessage;
import com.foxingarden.FoxInGarden.dto.PlayCardMessage;
import com.foxingarden.FoxInGarden.model.domain.Game;
import com.foxingarden.FoxInGarden.model.domain.Player;

@Service
public class GameEngineService { 
    
    @Autowired
    private Game game;

    public DetermineWinnerMessage determineWinner() {
        return game.determineWinner(new DetermineWinnerMessage());
    }

    public int playCard(PlayCardMessage playCardMessage) {
        String playerId = playCardMessage.getPlayerId();
        String suit = playCardMessage.getSuit();
        int rank = playCardMessage.getRank();

        game.playCard(playerId, suit, rank);
        return game.getCentralDeck().size();
    }

    public AddPlayerMessage addPlayer(String playerId) {
        Player newPlayer = game.addPlayer(playerId);
        return new AddPlayerMessage(newPlayer.getDeck());
    }

    public String getOtherPlayerId(String playerId) {
        return game.getOtherPlayerId(playerId);
    }
};