package com.foxingarden.FoxInGarden.model.domain;

import java.util.List;
import java.util.Collections;

public class Deck{

    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCard(int i) {
        return cards.get(i);
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

    public void addCard(Card card) {
        cards.add(card);
    }

    public int length() {
        return cards.size();
    }

    public Deck extractRandomCards(int n) {
        if (n>=cards.size())
            return this;
        Collections.shuffle(cards);
        List<Card> randomCards = cards.subList(0, n);
        return new Deck(randomCards);
    }
}
