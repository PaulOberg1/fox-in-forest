package com.foxingarden.FoxInGarden.model.domain;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;


import com.foxingarden.FoxInGarden.dto.DetermineWinnerMessage;

import lombok.Getter;

@Getter
public class Game {
    private Deck totalDeck;
    private List<Player> players;
    private int playerTurn;
    private Card decreeCard;
    private Map<String,Card> centralDeck;
    private Map<String,Player> idToPlayerMap;

    public Game (List<Player> players, int playerTurn) {
        this.totalDeck = generateTotalDeck();
        this.players = players;
        this.playerTurn = playerTurn;

        for (Player player : players) {
            idToPlayerMap.put(player.getId(),player);
        }
    }

    public Deck generateTotalDeck() {
        Deck curDeck = new Deck(new ArrayList<>());
        String[] suits = {"bell","key","moon"};
        for (String suit : suits) {
            for (int rank=1; rank<12; rank++) {
                curDeck.addCard(new Card(suit,rank));
            }
        }
        return curDeck;
    }

    public Player getPlayerById(String playerId) {
        return idToPlayerMap.get(playerId);
    }

    public Player addPlayer(String playerId) {
        Deck playerDeck = extractRandomDeck();
        Player player = new Player(playerId,playerDeck);
        players.add(player);
        idToPlayerMap.put(playerId, player);
        return player;
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

    public Deck extractRandomDeck() {
        return totalDeck.extractRandomCards(11);
    }

    public String getOtherPlayerId(String playerId) {
        if (playerId == players.get(0).getId()) 
            return players.get(1).getId();
        return players.get(0).getId();
    }


}
