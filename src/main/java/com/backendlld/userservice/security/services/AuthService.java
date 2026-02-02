package com.backendlld.userservice.security.services;

import com.backendlld.userservice.dtos.LoginRequestDto;
import com.backendlld.userservice.dtos.SignUpRequestDto;
import com.backendlld.userservice.dtos.LoginResponseDto;
import com.backendlld.userservice.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;


public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    UserDto signup(SignUpRequestDto signUpRequestDto);

    void logOut(Long id);

    UserDto updateUser(Long id,LoginRequestDto loginRequestDto);

    UserDto partialUpdateUser(Long id,java.util.Map<String,String> updates);

    ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId);
}
