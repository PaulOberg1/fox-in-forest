package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import com.foxingarden.FoxInGarden.dto.game_menu_dtos.AuthenticateUserMessage;
import com.foxingarden.FoxInGarden.dto.game_menu_dtos.BaseMenuMessage;
import com.foxingarden.FoxInGarden.service.GameMenuService;
import com.foxingarden.FoxInGarden.service.UserService;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
class GameMenuController{

    @Autowired
    GameMenuService gameMenuService;

    @Autowired
    UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, "{}");
    }

    @MessageMapping("/login")
    public void login(AuthenticateUserMessage authenticateUserMessage) { //add exception handling for username not existing
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = userService.getUserId(username,password);
        gameMenuService.registerClientUserMapping(clientId, userId);
        privateUpdate(clientId,"/p2p/homePage");
    }

    @MessageMapping("/signup")
    public void signup(AuthenticateUserMessage authenticateUserMessage) { //add exception handling for username already existing
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        long userId = userService.createUser(username,password);
        gameMenuService.registerClientUserMapping(clientId, userId);
        privateUpdate(clientId,"/p2p/homePage");
    }

    @MessageMapping("/connect")
    public void connect(BaseMenuMessage baseMessage) {
        String clientId = baseMessage.getClientId();
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        gameMenuService.registerClientSessionMapping(clientId, sessionId);
        privateUpdate(clientId, "/p2p/verifyConnect");
    }
}
