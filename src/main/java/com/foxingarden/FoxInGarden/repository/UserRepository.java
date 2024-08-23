package com.foxingarden.FoxInGarden.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxingarden.FoxInGarden.model.entity.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {

    public List<User> findByUsernameAndPassword(String username, String password);

}