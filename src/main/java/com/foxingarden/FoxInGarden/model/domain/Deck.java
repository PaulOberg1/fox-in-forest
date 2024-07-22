package com.foxingarden.FoxInGarden.model.domain;

import java.util.List;

public class Deck{

    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCard() {
        int size = cards.size();
        int index = (int)Math.floor(Math.random()*size);
        return cards.get(index);
    }

    public Card playCard(String suit, int rank) {
        for (Card card : cards) {
            if (card.getRank()==rank && card.getSuit()==suit) {
                cards.remove(card);
                return card;
            }
        }
        return null;
    }
}
