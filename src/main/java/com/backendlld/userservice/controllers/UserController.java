package com.backendlld.userservice.controllers;

import com.backendlld.userservice.dtos.SignUpRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//  http://localhost:8080/users/signup
@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public void signup(SignUpRequestDto signUpRequestDto) {

    }
}
