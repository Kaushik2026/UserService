package com.backendlld.userservice.security.successhandler;

import com.backendlld.userservice.dtos.LoginResponseDto;
import com.backendlld.userservice.security.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    //    here we will collect information from token provided by user authserver(google,github)
//    and create a new user in our db if not exists.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();
//        registration id tells which auth server is used like google,github
        String registrationId = token.getAuthorizedClientRegistrationId();

       ResponseEntity<LoginResponseDto> loginResponse = authService
               .handleOAuth2LoginRequest(oAuth2User,registrationId);

       response.setStatus(loginResponse.getStatusCode().value());
       response.setContentType(MediaType.APPLICATION_JSON_VALUE);
       response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
    }
}