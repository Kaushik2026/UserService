package com.backendlld.userservice.controllers;

import com.backendlld.userservice.dtos.LoginRequestDto;
import com.backendlld.userservice.dtos.LoginResponseDto;
import com.backendlld.userservice.dtos.SignUpRequestDto;
import com.backendlld.userservice.dtos.UserDto;
import com.backendlld.userservice.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto)  {
        return new ResponseEntity<>(authService.login(loginRequestDto), HttpStatus.CREATED);

    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody @Valid SignUpRequestDto signUpRequestDto)  {

        return new ResponseEntity<>(authService.signup(
                signUpRequestDto),HttpStatus.CREATED);
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<String> logOut(@PathVariable Long id) {
        authService.logOut(id);

        return new ResponseEntity<>("Logout Successfully",HttpStatus.OK);
    }
}
