package com.backendlld.userservice.controllers;

import com.backendlld.userservice.dtos.LogOutRequestDto;
import com.backendlld.userservice.dtos.LoginRequestDto;
import com.backendlld.userservice.dtos.SignUpRequestDto;
import com.backendlld.userservice.dtos.UserDto;
import com.backendlld.userservice.exceptions.InvalidTokenException;
import com.backendlld.userservice.exceptions.UserAlreadyExistsException;
import com.backendlld.userservice.models.Token;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) throws  Exception {
        Token token = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return token;
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException {

        User user = userService.signup(
                signUpRequestDto.getUsername(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword()
        );
        return UserDto.from(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogOutRequestDto logOutRequestDto) throws InvalidTokenException{
        userService.logOut(logOutRequestDto.getTokenValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validate-token/{tokenValue}")
    public UserDto validateToken(@PathVariable String tokenValue) throws InvalidTokenException {
        User user = userService.validateToken(tokenValue);
        return UserDto.from(user);
    }
}
