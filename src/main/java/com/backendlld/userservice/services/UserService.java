package com.backendlld.userservice.services;

import com.backendlld.userservice.exceptions.InvalidTokenException;
import com.backendlld.userservice.exceptions.InvalidUserNameOrPasswordException;
import com.backendlld.userservice.exceptions.UserNotFoundException;
import com.backendlld.userservice.models.Token;
import com.backendlld.userservice.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Token login(String email, String password) throws UserNotFoundException, InvalidUserNameOrPasswordException;

    User signup(String name, String email, String password);

    void logOut(String tokenValue);

    User validateToken(String tokenValue) throws InvalidTokenException;
}
