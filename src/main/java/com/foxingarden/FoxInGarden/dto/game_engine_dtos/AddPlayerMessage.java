package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Card;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage extends BaseEngineMessage {
    private List<Card> cardList;
    private int numPlayers;

    public AddPlayerMessage(String clientId, String gameId, List<Card> cardList, int numPlayers) {
        super(clientId,gameId);
        this.cardList = cardList;
        this.numPlayers = numPlayers;
    }
}
