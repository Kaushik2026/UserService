// java
//package com.backendlld.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfiguration {
//    as springboot security dependency is added, by default all the endpoints are secured
//    to disable security for all endpoints, uncomment the below code. i.e we have created a
//    bean of type SecurityFilterChain so that spring boot picks this configuration instead of default one and we can
//    login,signup without any issues. means we are just allowing all the requests without authentication.

//    @Bean
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
//            throws Exception {
//
//        http
//                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }

//}
