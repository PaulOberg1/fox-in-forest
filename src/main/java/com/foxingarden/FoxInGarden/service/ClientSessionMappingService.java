package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

public class ClientSessionMappingService {
    
    private final ConcurrentHashMap<String,String> clientIdToSessionId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,String> sessionIdToClientId = new ConcurrentHashMap<>();

    public void registerClient(String clientId, String sessionId) {
        clientIdToSessionId.putIfAbsent(clientId, sessionId);
        sessionIdToClientId.putIfAbsent(sessionId, clientId);
    }
    
    public String getSessionId(String clientId) {
        return clientIdToSessionId.get(clientId);
    }

    public String getClientId(String sessionId) {
        return sessionIdToClientId.get(sessionId);
    }

    public void removeClient(String clientId) {
        String sessionId = clientIdToSessionId.get(clientId);
        clientIdToSessionId.remove(clientId);
        sessionIdToClientId.remove(sessionId);
    }
}
