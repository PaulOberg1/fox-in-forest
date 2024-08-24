package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

import com.foxingarden.FoxInGarden.model.domain.Player;

public class ClientPlayerMappingService {
    
    private final ConcurrentHashMap<String,Player> clientIdToPlayerId = new ConcurrentHashMap<>();

    public void registerClient(String clientId, Player player) {
        clientIdToPlayerId.putIfAbsent(clientId, player);
    }
    
    public Player getPlayer(String clientId) {
        return clientIdToPlayerId.get(clientId);
    }
    
    public void removeClient(String clientId) {
        clientIdToPlayerId.remove(clientId);
    }
}
