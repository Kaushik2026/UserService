package com.backendlld.userservice.dtos;

import com.backendlld.userservice.models.Role;
import com.backendlld.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private List<Role> roles;

    public static UserDto from(User user){
        if(user == null){
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
