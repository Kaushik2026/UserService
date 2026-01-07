//package com.backendlld.userservice.controllers;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class RootController {
//    @GetMapping("/")
//    public ResponseEntity<String> home(Authentication auth) {
//        return ResponseEntity.ok("Welcome " + auth.getName() +
//            " | Token login: POST /users/login");
//    }
//}
