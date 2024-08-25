package com.foxingarden.FoxInGarden.model.domain;

import java.util.Map;
import java.util.ArrayList;

import lombok.Getter;

@Getter
public class GameSession {
    private final String id;
    private Game game;

    private Map<String,Player> idToPlayerMap;

    public GameSession(String id, Game game) {
        this.id = id;
        this.game = game;
    }

    public void addPlayer(String playerId) {
        Player player = game.addPlayer(playerId);
        idToPlayerMap.put(playerId,player);
    }

    public Player getPlayerById(String playerId) {
        return idToPlayerMap.get(playerId);
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(idToPlayerMap.values());
    }

    public Player getOtherPlayerById(String playerId) {
        ArrayList<String> playerIds = new ArrayList<>(idToPlayerMap.keySet());
        String otherPlayerId;
        if (playerIds.get(0)==playerId) 
            otherPlayerId = playerIds.get(1);
        else 
            otherPlayerId = playerIds.get(0);
        return idToPlayerMap.get(otherPlayerId);
    }

}
