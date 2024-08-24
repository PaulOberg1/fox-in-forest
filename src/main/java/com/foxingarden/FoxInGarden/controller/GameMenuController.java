package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import com.foxingarden.FoxInGarden.dto.AuthenticateUserMessage;
import com.foxingarden.FoxInGarden.dto.BaseMessage;
import com.foxingarden.FoxInGarden.service.GameMenuService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
class GameMenuController{

    @Autowired
    GameMenuService gameMenuService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void privateUpdate(String userId, String path, BaseMessage dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, null);
    }

    @MessageMapping("/login")
    public void login(AuthenticateUserMessage authenticateUserMessage) {
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = gameMenuService.getUserId(username,password);
        gameMenuService.registerClientUserMapping(clientId, userId);
        privateUpdate(clientId,"p2p/loginUpdate");
    }

    @MessageMapping("/signup")
    public void signup(AuthenticateUserMessage authenticateUserMessage) {
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = gameMenuService.createUser(username,password);
        gameMenuService.registerClientUserMapping(clientId, userId);
        privateUpdate(clientId,"p2p/signupUpdate");
    }

    @MessageMapping("/connect")
    public void connect(BaseMessage baseMessage) {
        String clientId = baseMessage.getClientId();
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        gameMenuService.registerClientSessionMapping(clientId, sessionId);
    }
}