package com.backendlld.userservice.services;

import com.backendlld.userservice.dtos.UserDto;

public interface AdminService {

    UserDto assignRoles(Long id, String value);
}
