package com.backendlld.userservice.models;

import com.backendlld.userservice.models.enums.AuthProviderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel implements UserDetails {
    private String username;
    private String email;
    private String password;
    private boolean loggedIn;

    @Enumerated(EnumType.STRING)
    private AuthProviderType providerType;

    private String providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"user"})
    private List<Role> roles=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getValue()))
                .collect(Collectors.toSet());
    }
}
