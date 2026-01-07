package com.backendlld.userservice.services;

import com.backendlld.userservice.exceptions.*;
import com.backendlld.userservice.models.Token;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.repositories.TokenRepository;
import com.backendlld.userservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
//    passwordencoder cant be autowired directly without configuring security configuration so
//    we have to create a bean for it in config class
    private final PasswordEncoder passwordEncoder;
    @Override
    public Token login(String email, String password) throws UserNotFoundException, InvalidUserNameOrPasswordException
    , UserAlreadyLoggedInException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User existingUser = user.get();
            if(existingUser.isLoggedIn()){
                throw new UserAlreadyLoggedInException("User already logged in");
            }

            if (passwordEncoder.matches(password, existingUser.getPassword())) {
                // In real application, generate JWT or session token here
                Token token = new Token();
                token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
                token.setUser(existingUser);
                Date expiry = Date.from(LocalDateTime.now().plusDays(30)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
                token.setExpiryDate(expiry);
                existingUser.setLoggedIn(true);
                return tokenRepository.save(token);
            } else {
                throw new InvalidUserNameOrPasswordException("Invalid username or password");
            }
        } else {
//            redirect to signup page
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public User signup(String name, String email, String password) throws UserAlreadyExistsException{
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
//            we should not throw error here instead we should redirect to login page.
//            throw new RuntimeException("User already exists with email: " + email);
            throw new UserAlreadyExistsException("User already exists with email: " + email+". Please login instead." );
        }
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);

        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(new ArrayList<>());
        return userRepository.save(newUser);
    }

    @Override
    public void logOut(String tokenValue) throws InvalidTokenException{
        Optional<Token> token = tokenRepository.findByTokenValue(tokenValue);
        if(token.isEmpty()){
            throw new InvalidTokenException("Invalid token");
        }
        User user = token.get().getUser();
        user.setLoggedIn(false);
        userRepository.save(user);
        token.get().setDeleted(true);
        tokenRepository.save(token.get());
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException{
        Optional<Token> token = tokenRepository.findByTokenValueAndDeletedFalseAndExpiryDateGreaterThan(
                tokenValue,
                new Date()
        );
        if(token.isEmpty()){
            throw new InvalidTokenException("Token is invalid or expired");
        }
        return token.get().getUser();

    }
}
