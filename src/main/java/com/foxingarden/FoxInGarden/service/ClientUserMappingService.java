package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

public class ClientUserMappingService {
    
    private final ConcurrentHashMap<String,Long> clientIdToUserId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long,String> userIdToClientId = new ConcurrentHashMap<>();

    public void registerClient(String clientId, long userId) {
        clientIdToUserId.putIfAbsent(clientId, userId);
        userIdToClientId.putIfAbsent(userId, clientId);
    }
    
    public long getUserId(String clientId) {
        return clientIdToUserId.get(clientId);
    }

    public String getClientId(long userId) {
        return userIdToClientId.get(userId);
    }

    public void removeClient(String clientId) {
        long userId = clientIdToUserId.get(clientId);
        clientIdToUserId.remove(clientId);
        userIdToClientId.remove(userId);
    }

    public void removeClient(long userId) {
        String clientId = userIdToClientId.get(userId);
        userIdToClientId.remove(userId);
        clientIdToUserId.remove(clientId);
    }

}
