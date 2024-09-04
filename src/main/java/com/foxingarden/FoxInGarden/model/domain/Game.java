package com.foxingarden.FoxInGarden.model.domain;


import com.foxingarden.FoxInGarden.dto.game_engine_dtos.CentralDeckMessage;
import com.foxingarden.FoxInGarden.dto.game_engine_dtos.EndGameMessage;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class Game {
    private String id;
    private Deck totalDeck;
    private ArrayList<Tuple<Player,Card>> centralCards;
    private Player curPlayer;
    private Card decreeCard;
    private String roundWinner;

    public Game (String id) {
        this.id = id;
        this.totalDeck = generateTotalDeck();
        this.decreeCard = null;
        this.centralCards = new ArrayList<>();
    }

    public Card extractDecreeCard() {
        Card c = totalDeck.extractRandomCards(1).getCard(0);
        decreeCard = c;
        return c;
    }

    public Player addPlayer(String playerId) {
        Deck playerDeck = extractRandomDeck();
        Player player = new Player(playerId,playerDeck);
        return player;
    }

    public Deck extractRandomDeck() {
        return totalDeck.extractRandomCards(13); //change back to/keep at 13
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
        if (centralCards.size() == 2) {
            centralCards.clear();
        }
        if (player.getId().equals(curPlayer.getId()) && player.playCard(suit, rank)) {
            centralCards.add(new Tuple<Player,Card>(player, new Card(suit,rank)));
        }
        if (centralCards.size() == 2) {
            byte winner = computeWinner(centralCards.get(0).y, centralCards.get(1).y, decreeCard);
            centralCards.get(winner).x.updateScore();
            roundWinner = centralCards.get(winner).x.getId();
        }
    }

    public CentralDeckMessage getCentralDeckState(String clientId) {
        ArrayList<String> playerIds = new ArrayList<>();
        ArrayList<String> cardSuits = new ArrayList<>();
        ArrayList<Integer> cardRanks = new ArrayList<>();

        for (Tuple<Player,Card> entry : centralCards) {
            Card card = entry.y;
            String playerId = entry.x.getId();

            playerIds.add(playerId);
            cardSuits.add(card.getSuit());
            cardRanks.add(card.getRank());
        }
        return new CentralDeckMessage(clientId, id, playerIds, cardSuits, cardRanks, roundWinner, centralCards.size() == 2);
    }

    public boolean isEnded(ArrayList<Player> players) {
        return players.get(0).getDeck().length() == 0 && players.get(1).getDeck().length() == 0;
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

    public byte computeWinner(Card card1, Card card2, Card decreeCard) {
        String trumpSuit = decreeCard.getSuit();
        if (card1.getSuit().equals(trumpSuit) && card2.getSuit().equals(trumpSuit)) {
            if (card1.getRank()>card2.getRank())
                return 0;
            return 1;
        }
        else if (card1.getSuit().equals(trumpSuit))
            return 0;
        else if (card2.getSuit().equals(trumpSuit))
            return 1;
        else {
            if (!card1.getSuit().equals(card2.getSuit()))
                return 0;
            else if (card1.getRank()>card2.getRank())
                return 0;
            else
                return 1;
        }
    }
}

class Tuple<X, Y> { 
    public X x; 
    public Y y; 
    public Tuple(X x, Y y) { 
      this.x = x; 
      this.y = y; 
    } 
  }
