package com.foxingarden.FoxInGarden.dto;

import com.foxingarden.FoxInGarden.model.domain.Deck;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage {
    private Deck deck;

    public AddPlayerMessage(Deck deck) {
        this.deck = deck;
    }
}
