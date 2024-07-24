package com.foxingarden.FoxInGarden.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.foxingarden.FoxInGarden.model.entity.User;
import com.foxingarden.FoxInGarden.service.UserService;


@RestController
@RequestMapping("/menu")
class GameMenuController{

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody User user) {
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
    }
}