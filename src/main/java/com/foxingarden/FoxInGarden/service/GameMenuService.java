package com.foxingarden.FoxInGarden.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.model.entity.User;
import com.foxingarden.FoxInGarden.repository.UserRepository;

@Service
public class GameMenuService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ClientUserMappingService clientUserMappingService;

    @Autowired
    ClientSessionMappingService clientSessionMappingService;

    private User user;

    private final ConcurrentHashMap<String,String> clientIdToSessionId = new ConcurrentHashMap<>();

    public long getUserId(String username, String password) {
        this.user = userRepository.findByUsernameAndPassword(username,password).get(0);
        return user.getId();
    }

    public long createUser(String username, String password) {
        User user = new User(username,password);
        userRepository.save(user);
        return user.getId();
    }

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
