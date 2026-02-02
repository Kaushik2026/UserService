package com.backendlld.userservice.repositories;

import com.backendlld.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {


    Optional<Role> findByValueIgnoreCase(String value);
}
