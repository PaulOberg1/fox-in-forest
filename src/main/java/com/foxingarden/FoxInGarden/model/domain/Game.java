package com.foxingarden.FoxInGarden.model.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;

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

}
