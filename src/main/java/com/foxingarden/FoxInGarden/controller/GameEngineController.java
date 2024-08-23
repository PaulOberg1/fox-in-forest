package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RestController;

import com.foxingarden.FoxInGarden.dto.BaseDTO;

import com.foxingarden.FoxInGarden.service.GameEngineService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;



@RestController
class GameEngineController{

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastUpdate(String path, BaseDTO dto) {
        messagingTemplate.convertAndSend(path, dto);
    }

    public void privateUpdate(String userId, String path, BaseDTO dto) {
        messagingTemplate.convertAndSendToUser(userId, path, dto);
    }
}