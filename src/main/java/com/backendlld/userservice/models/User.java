package com.backendlld.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel {
    private String username;
    private String email;
    private String password;
    private boolean loggedIn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"user"})
    private List<Role> roles;
}
