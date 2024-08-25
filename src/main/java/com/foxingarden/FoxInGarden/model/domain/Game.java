package com.foxingarden.FoxInGarden.model.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Game {
    private String id;
    private Deck totalDeck;
    private ConcurrentHashMap<String,Card> centralCards = new ConcurrentHashMap<>();
    private Player curPlayer;

    public Game (String id) {
        this.id = id;
        this.totalDeck = generateTotalDeck();
    }

    public Player addPlayer(String playerId) {
        Deck playerDeck = extractRandomDeck();
        Player player = new Player(playerId,playerDeck);
        return player;
    }

    public Deck extractRandomDeck() {
        return totalDeck.extractRandomCards(11);
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

    public void setCurPlayer(Player player) {
        curPlayer = player;
    }

    public void playCard(Player player, String suit, int rank) {
        player.playCard(suit, rank);
        centralCards.put(player.getId(), new Card(suit, rank));
    }

    public CentralDeckMessage getCentralDeckState(String clientId) {
        ArrayList<String> playerIds = new ArrayList<>();
        ArrayList<String> cardSuits = new ArrayList<>();
        ArrayList<Integer> cardRanks = new ArrayList<>();

        for (Map.Entry<String,Card> entry : centralCards.entrySet()) {
            String playerId = entry.getKey();
            Card card = entry.getValue();
            
            playerIds.add(playerId);
            cardSuits.add(card.getSuit());
            cardRanks.add(card.getRank());
        }
        return new CentralDeckMessage(clientId, id, playerIds, cardSuits, cardRanks);
    }

    public boolean isEnded() {
        return totalDeck.length()==0;
    }

    public EndGameMessage endGame(String clientId, ArrayList<Player> players) {
        Player player1 = players.get(0);
        int player1Points = computePointsFromScore(player1.getScore());

        Player player2 = players.get(1);
        int player2Points = computePointsFromScore(player2.getScore());

        return new EndGameMessage(clientId, id, player1.getId(), player2.getId(), player1Points, player2Points);

    }

    public int computePointsFromScore(int score) {
        if (score<=3)
            return 6;
        else if (score==4)
            return 1;
        else if (score==5)
            return 2;
        else if (score==6)
            return 3;
        else if (score<=9)
            return 6;
        else
            return 0;
    }

}
