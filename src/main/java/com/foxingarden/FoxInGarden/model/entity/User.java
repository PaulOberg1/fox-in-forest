package com.foxingarden.FoxInGarden.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import lombok.Setter;
import lombok.Getter;

@Entity
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "user username required")
    private String username;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "user password required")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    

}
