package com.backendlld.userservice.repositories;

import com.backendlld.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    Optional<Token> findByTokenValue(String tokenValue);

    Optional<Token> findByTokenValueAndDeletedFalseAndExpiryDateGreaterThan(String tokenValue,
                                                                            Date currentDate);
}
