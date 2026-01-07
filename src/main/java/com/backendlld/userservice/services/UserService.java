package com.backendlld.userservice.services;

import com.backendlld.userservice.exceptions.*;
import com.backendlld.userservice.models.Token;
import com.backendlld.userservice.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Token login(String email, String password) throws UserNotFoundException, InvalidUserNameOrPasswordException, UserAlreadyLoggedInException;

    User signup(String name, String email, String password) throws UserAlreadyExistsException;

    void logOut(String tokenValue) throws InvalidTokenException;

    User validateToken(String tokenValue) throws InvalidTokenException;
}
