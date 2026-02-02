package com.backendlld.userservice.controllers;

import com.backendlld.userservice.dtos.*;
import com.backendlld.userservice.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final AuthService authService;

    @GetMapping("/sample")
    public void sampleEndpoint() {
        System.out.println("Call recieved");
    }



    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid LoginRequestDto loginRequestDto)  {
        return new ResponseEntity<>(authService.updateUser(id,loginRequestDto),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdateUser(@PathVariable Long id,@RequestBody Map<String,String> updates)  {
        return new ResponseEntity<>(authService.partialUpdateUser(id,updates),HttpStatus.OK);
    }
}
