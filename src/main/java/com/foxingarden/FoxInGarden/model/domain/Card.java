package com.foxingarden.FoxInGarden.model.domain;

public class Card {
    private String suit;
    private int rank;

    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getSuit() {
        return suit;
    }
    public int getRank() {
        return rank;
    }
}
