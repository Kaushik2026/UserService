package com.backendlld.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "roles")
public class Role extends BaseModel {
    private String value;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> user;
}
