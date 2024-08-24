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

    public void registerClient(String clientId, String sessionId) {
        clientIdToSessionId.putIfAbsent(clientId, sessionId);
    }
    
    public String getSessionId(String clientId) {
        return clientIdToSessionId.get(clientId);
    }

    public void removeClient(String clientId) {
        clientIdToSessionId.remove(clientId);
    }


}
