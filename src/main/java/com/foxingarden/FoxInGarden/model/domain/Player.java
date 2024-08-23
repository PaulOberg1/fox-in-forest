package com.foxingarden.FoxInGarden.model.domain;


import lombok.Getter;

@Getter
public class Player {

    private String id;
    private Deck deck;
    private Card currentCardPlayed;
    private int score;

    public Player(String id, Deck deck) {
        this.id = id;
        this.deck = deck;
        this.currentCardPlayed = null;
        this.score=0;
    }
    public void playCard(String suit, int rank) {
        currentCardPlayed = deck.playCard(suit,rank);
    }
    public void updateScore() {
        this.score++;
    }
}
