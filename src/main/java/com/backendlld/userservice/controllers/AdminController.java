package com.backendlld.userservice.controllers;

import com.backendlld.userservice.dtos.UserDto;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.services.AdminService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //    assign roles
    @PutMapping("/assign-roles/{id}/{value}")
    public ResponseEntity<UserDto> assignRoles(@PathVariable Long id, @PathVariable String value) {
        return new ResponseEntity<>(adminService.assignRoles(id,value), HttpStatus.OK);
    }
}
