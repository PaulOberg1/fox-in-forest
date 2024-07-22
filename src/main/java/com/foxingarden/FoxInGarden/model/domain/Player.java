package com.foxingarden.FoxInGarden.model.domain;

public class Player {

    private Deck deck;

    public Player(Deck deck) {
        this.deck = deck;
    }
    public Card playCard(String suit, int rank) {
        return deck.playCard(suit,rank);
    }
}
