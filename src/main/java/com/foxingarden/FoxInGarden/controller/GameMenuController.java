package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import com.foxingarden.FoxInGarden.dto.game_menu_dtos.AuthenticateUserMessage;
import com.foxingarden.FoxInGarden.dto.game_menu_dtos.BaseMenuMessage;
import com.foxingarden.FoxInGarden.dto.game_alert_dtos.ServerAlertMessage;
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

    public void privateUpdate(String userId, String path, ServerAlertMessage dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }

    public void privateUpdate(String userId, String path) {
        messagingTemplate.convertAndSendToUser(userId, path, "{}");
    }

    @MessageMapping("/login")
    public void login(AuthenticateUserMessage authenticateUserMessage) {
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        try {
            long userId = userService.getUserId(username,password);
            gameMenuService.registerClientUserMapping(clientId, userId);
            privateUpdate(clientId,"/p2p/homePage");
        }
        catch (Exception e) {
            privateUpdate(clientId,"/p2p/serverAlertMessage",new ServerAlertMessage("Incorrect username/password"));
        }
    }

    @MessageMapping("/signup")
    public void signup(AuthenticateUserMessage authenticateUserMessage) {
        String clientId = authenticateUserMessage.getClientId();
        String username = authenticateUserMessage.getUsername();
        String password = authenticateUserMessage.getPassword();
        try {
            long userId = userService.createUser(username,password);
            gameMenuService.registerClientUserMapping(clientId, userId);
            privateUpdate(clientId,"/p2p/homePage");
        }
        catch (Exception e) {
            privateUpdate(clientId,"/p2p/serverAlertMessage",new ServerAlertMessage("This username already exists"));
        }
    }

    @MessageMapping("/connect")
    public void connect(BaseMenuMessage baseMessage) {
        String clientId = baseMessage.getClientId();
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        gameMenuService.registerClientSessionMapping(clientId, sessionId);
        privateUpdate(clientId, "/p2p/verifyConnect");
    }
}
