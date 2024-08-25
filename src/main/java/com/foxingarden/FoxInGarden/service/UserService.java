package com.foxingarden.FoxInGarden.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.model.entity.User;
import com.foxingarden.FoxInGarden.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private User user;

    public long getUserId(String username, String password) {
        this.user = userRepository.findByUsernameAndPassword(username,password).get(0);
        return user.getId();
    }

    public long createUser(String username, String password) {
        User user = new User(username,password);
        userRepository.save(user);
        return user.getId();
    }


}
