package com.foxingarden.FoxInGarden.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foxingarden.FoxInGarden.model.entity.User;
import com.foxingarden.FoxInGarden.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).get(0);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }


}
