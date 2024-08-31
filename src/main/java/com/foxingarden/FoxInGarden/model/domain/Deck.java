package com.foxingarden.FoxInGarden.model.domain;

import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class Deck{

    private List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
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
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getRank() == rank && card.getSuit().equals(suit)) {
                iterator.remove();
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
        if (n>=cards.size()) {
            Deck extractedDeck = new Deck(new ArrayList<>(cards));
            cards.clear();
            return extractedDeck;
        }
        Collections.shuffle(cards);
        List<Card> randomCards = new ArrayList<>(cards.subList(0, n));
        cards.subList(0,n).clear();
        return new Deck(randomCards);
    }
}
