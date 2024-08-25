package com.foxingarden.FoxInGarden.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameMenuService {

    @Autowired
    ClientUserMappingService clientUserMappingService;

    @Autowired
    ClientSessionMappingService clientSessionMappingService;

    public String getSessionId(String clientId) {
        return clientSessionMappingService.getSessionId(clientId);
    }

    public long getUserId(String clientId) {
        return clientUserMappingService.getUserId(clientId);
    }

    public void removeClient(String clientId) {
        clientUserMappingService.removeClient(clientId);
        clientSessionMappingService.removeClient(clientId);
    }

    public void registerClientSessionMapping(String clientId, String sessionId) {
        clientSessionMappingService.registerClient(clientId, sessionId);
    }

    public void registerClientUserMapping(String clientId, long userId) {
        clientUserMappingService.registerClient(clientId, userId);
    }


}
