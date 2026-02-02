package com.backendlld.userservice.services;

import com.backendlld.userservice.dtos.UserDto;
import com.backendlld.userservice.models.Role;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.repositories.RoleRepository;
import com.backendlld.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto assignRoles(Long id, String value) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        // Logic to assign roles to the user
        Role role = roleRepository.findByValueIgnoreCase(value)  // Fetch existing!
                .orElseThrow(() -> new IllegalArgumentException("Role not found for: " + value));

        if (user.getRoles().contains(role)) {
            throw new IllegalArgumentException("Role already assigned");
        }

        user.getRoles().add(role);
        role.getUser().add(user);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }
}
