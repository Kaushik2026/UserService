package com.backendlld.userservice.services;

import com.backendlld.userservice.dtos.LoginRequestDto;
import com.backendlld.userservice.dtos.SignUpRequestDto;
import com.backendlld.userservice.dtos.LoginResponseDto;
import com.backendlld.userservice.dtos.UserDto;
import com.backendlld.userservice.models.Token;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.repositories.TokenRepository;
import com.backendlld.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

//    passwordencoder cant be autowired directly without configuring security configuration so
//    we have to create a bean for it in config class
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User existingUser = user.get();
            if(existingUser.isLoggedIn()){
                throw new IllegalArgumentException("User already logged in");
            }

            if (passwordEncoder.matches(password, existingUser.getPassword())) {
                Token token = new Token();
                token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
                token.setUser(existingUser);
                Date expiry = Date.from(LocalDateTime.now().plusDays(30)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
                token.setExpiryDate(expiry);
                existingUser.setLoggedIn(true);
                Token savedToken = tokenRepository.save(token);
                return modelMapper.map(savedToken, LoginResponseDto.class);
            } else {
                throw new IllegalArgumentException("Invalid username or password");
            }
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public UserDto signup(SignUpRequestDto signUpRequestDto){
        String name = signUpRequestDto.getUsername();
        String email = signUpRequestDto.getEmail();
        String password = signUpRequestDto.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
//            we should not throw error here instead we should redirect to login page.
//            throw new RuntimeException("User already exists with email: " + email);
            throw new IllegalArgumentException("User already exists with email: " + email+". Please login instead." );
        }
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);

        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRoles(new ArrayList<>());
        User savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void logOut(String tokenValue){
        Optional<Token> token = tokenRepository.findByTokenValue(tokenValue);
        if(token.isEmpty()){
            throw new IllegalArgumentException("Invalid token");
        }
        User user = token.get().getUser();
        user.setLoggedIn(false);
        userRepository.save(user);
        token.get().setDeleted(true);
        tokenRepository.save(token.get());
    }

    @Override
    public UserDto validateToken(String tokenValue){
        Optional<Token> token = tokenRepository.findByTokenValueAndDeletedFalseAndExpiryDateGreaterThan(
                tokenValue,
                new Date()
        );
        if(token.isEmpty()){
            throw new IllegalArgumentException("Token is invalid or expired");
        }
        return modelMapper.map(token.get().getUser(), UserDto.class);

    }

    @Override
    public UserDto updateUser(Long id,LoginRequestDto loginRequestDto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id: "+id));

        modelMapper.map(loginRequestDto, user);
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto partialUpdateUser(Long id, Map<String, String> updates){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id: "+id));

        updates.forEach((field,value) -> {
            switch (field){
                case "username":
                    user.setUsername(value);
                    break;
                case "email":
                    user.setEmail(value);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid feild");
            }
        });
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }
}
