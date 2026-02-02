package com.backendlld.userservice.dtos;

import com.backendlld.userservice.models.Role;
import com.backendlld.userservice.models.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private List<String> roles;


//    public static UserDto from(User user){
//        if(user == null){
//            return null;
//        }
//        UserDto userDto = new UserDto();
//        userDto.setUsername(user.getUsername());
//        userDto.setEmail(user.getEmail());
//        userDto.setRoles(new ArrayList<>());
//
//        for (Role role : user.getRoles()) {
//            if (role.getValue() != null) {
//                userDto.getRoles().add(role.getValue());
//            }
//        }
//        return userDto;
//    }
}
