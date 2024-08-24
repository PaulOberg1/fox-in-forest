package com.foxingarden.FoxInGarden.dto;

import com.foxingarden.FoxInGarden.model.domain.Deck;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage extends BaseDTO {
    private Deck deck;

    public AddPlayerMessage(String clientId, Deck deck) {
        super(clientId);
        this.deck = deck;
    }
}
