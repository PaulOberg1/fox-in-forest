package com.foxingarden.FoxInGarden.model.domain;

import java.util.Map;
import java.util.ArrayList;

import lombok.Getter;

@Getter
public class GameSession {
    private final String id;
    private Game game;

    private Map<String,Player> idToPlayerMap;
    private ArrayList<Player> players;

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
        Player otherPlayer;
        if (players.get(0).getId()==playerId) 
            otherPlayer = players.get(1);
        else 
            otherPlayer = players.get(0);
        return otherPlayer;
    }

}
