package com.backendlld.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "roles")
public class Role extends Base{
    private String value;

    @ManyToMany(mappedBy = "roles")
    private List<User> user;
}
