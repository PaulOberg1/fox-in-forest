package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

public class UserMappingService {
    
    private final ConcurrentHashMap<String,Long> clientIdToPlayerId = new ConcurrentHashMap<>();

    public void registerClient(String clientId, long playerId) {
        clientIdToPlayerId.putIfAbsent(clientId, playerId);
    }
    
    public long getPlayerId(String clientId) {
        return clientIdToPlayerId.get(clientId);
    }

    public void removeClient(String clientId) {
        clientIdToPlayerId.remove(clientId);
    }

}
