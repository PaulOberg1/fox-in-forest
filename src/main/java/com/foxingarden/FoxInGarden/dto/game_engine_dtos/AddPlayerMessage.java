package com.foxingarden.FoxInGarden.dto.game_engine_dtos;

import com.foxingarden.FoxInGarden.model.domain.Card;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class AddPlayerMessage extends BaseEngineMessage {
    private List<Card> cardList;

    public AddPlayerMessage(String clientId, String gameId, List<Card> cardList) {
        super(clientId,gameId);
        this.cardList = cardList;
    }
}
