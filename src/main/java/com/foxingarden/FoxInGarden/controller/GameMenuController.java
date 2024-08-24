package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxingarden.FoxInGarden.dto.AuthenticateUserMessage;
import com.foxingarden.FoxInGarden.dto.BaseDTO;
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

    public void privateUpdate(String userId, String path, BaseDTO dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, null);
    }

    @MessageMapping("/login")
    public void login(AuthenticateUserMessage authenticateUserMessage) {
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = gameMenuService.getUserId(username,password);
        privateUpdate(String.valueOf(userId),"p2p/loginUpdate");
    }

    @MessageMapping("/signup")
    public void signup(AuthenticateUserMessage authenticateUserMessage) {
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = gameMenuService.createUser(username,password);
        privateUpdate(String.valueOf(userId),"p2p/signupUpdate");
    }

    @MessageMapping("/connect")
    public void connect(String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);
            String clientId = jsonNode.get("clientId").asText();
            String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
            gameMenuService.registerClient(clientId, sessionId);
        }
        catch (IOException e) {
            e.printStackTrace();
        }   
    }
}