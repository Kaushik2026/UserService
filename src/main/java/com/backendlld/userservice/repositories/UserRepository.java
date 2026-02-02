package com.backendlld.userservice.repositories;

import com.backendlld.userservice.models.User;
import com.backendlld.userservice.models.enums.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);

}
