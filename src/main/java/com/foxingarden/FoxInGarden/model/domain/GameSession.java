package com.foxingarden.FoxInGarden.model.domain;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import lombok.Getter;

@Getter
public class GameSession {
    private final String id;
    private Game game;

    private Map<String,Player> idToPlayerMap;
    private ArrayList<Player> players;

    public GameSession(String id) {
        this.id = id;
        this.game = new Game(id);
        this.idToPlayerMap = new HashMap<String,Player>();
        this.players = new ArrayList<Player>();
    }

    public void addPlayer(String playerId) {
        Player player = game.addPlayer(playerId);
        idToPlayerMap.put(playerId,player);
        players.add(player);
    }

    public Player getPlayerById(String playerId) {
        return idToPlayerMap.get(playerId);
    }

    public Player getOtherPlayerById(String playerId) {
        if (players.get(0).getId().equals(playerId))
            return players.get(1);
        return players.get(0);
    }

}
