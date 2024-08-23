package com.foxingarden.FoxInGarden.model.domain;

import java.util.List;
import java.util.Map;

import com.foxingarden.FoxInGarden.dto.DetermineWinnerMessage;

import lombok.Getter;

@Getter
public class Game {
    private Deck drawDeck;
    private List<Player> players;
    private int playerTurn;
    private Card decreeCard;
    private Map<String,Card> centralDeck;
    private Map<String,Player> idToPlayerMap;

    public Game (Deck drawDeck, List<Player> players, int playerTurn) {
        this.drawDeck = drawDeck;
        this.players = players;
        this.playerTurn = playerTurn;
        this.decreeCard = drawDeck.getCard(0);

        for (Player player : players) {
            idToPlayerMap.put(player.getId(),player);
        }
    }

    public Player getPlayerById(String playerId) {
        return idToPlayerMap.get(playerId);
    }

    public DetermineWinnerMessage determineWinner(DetermineWinnerMessage determineWinnerMessage) {
        Player player1 = players.get(0);
        determineWinnerMessage.setPlayer1Id(player1.getId());
        determineWinnerMessage.setPlayer1Score(player1.getScore());

        Player player2 = players.get(0);
        determineWinnerMessage.setPlayer2Id(player2.getId());
        determineWinnerMessage.setPlayer2Score(player2.getScore());

        if (determineWinnerMessage.getPlayer1Score()>determineWinnerMessage.getPlayer2Score())
            determineWinnerMessage.setWinnerId(determineWinnerMessage.getPlayer1Id());
        else
            determineWinnerMessage.setWinnerId(determineWinnerMessage.getPlayer2Id());

        return determineWinnerMessage;
        
    }


    public void playCard(String playerId, String suit, int rank) {
        Player player = idToPlayerMap.get(playerId);
        player.playCard(suit, rank);

        centralDeck.put(playerId, player.getCurrentCardPlayed());
    }

}
