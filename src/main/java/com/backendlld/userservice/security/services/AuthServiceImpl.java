package com.backendlld.userservice.security.services;

import com.backendlld.userservice.dtos.*;
import com.backendlld.userservice.models.Role;
import com.backendlld.userservice.models.User;
import com.backendlld.userservice.models.enums.AuthProviderType;
import com.backendlld.userservice.repositories.RoleRepository;
import com.backendlld.userservice.repositories.UserRepository;
import com.backendlld.userservice.security.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Primary
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;
    private static final String DEFAULT_ROLE = "USER";

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
//        User user = (User) authentication.getPrincipal();
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.isLoggedIn()){
            throw new IllegalArgumentException("User already logged in");
        }
        String token = jwtUtil.generateToken(user);
        user.setLoggedIn(true);
        userRepository.save(user);
        return new LoginResponseDto(token,user.getId());

    }

    public UserDto signup(SignUpRequestDto signupRequestDto){
        User user = signUpInternal(signupRequestDto,AuthProviderType.EMAIL,null);
        return modelMapper.map(user,UserDto.class);
    }

    public User signUpInternal(SignUpRequestDto signUpRequestDto,AuthProviderType authProviderType,String providerId){
        String name = signUpRequestDto.getUsername();
        String email = signUpRequestDto.getEmail();
        String password = signUpRequestDto.getPassword();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new IllegalArgumentException("User already exists with email: " + email+". Please login instead." );
        }
        User newUser = User.builder()
                .email(email)
                .username(name)
                .providerId(providerId)
                .providerType(authProviderType)
                .build();

        Role role = roleRepository.findByValueIgnoreCase(DEFAULT_ROLE)  // Fetch existing
                .orElseThrow(() -> new IllegalArgumentException("Role not found for: " + "USER"));
        newUser.getRoles().add(role);
        role.getUser().add(newUser);//for by-directional mapping

        if(authProviderType == AuthProviderType.EMAIL) {
            newUser.setPassword(passwordEncoder.encode(password));
        }
        newUser = userRepository.save(newUser);
        SendEmailDto sendEmailDto = SendEmailDto.builder()
                .to(email)
                .from("admin@gmail.com")
                .subject("Welcome to our platform")
                .body("Hello "+name+",\n\nWelcome to our platform! We're excited to have you on board." +
                        "\n\nBest regards,\nThe Team")
                .build();
        //send welcome email using kafka
        try {
            kafkaTemplate.send("sendEmail", objectMapper.writeValueAsString(sendEmailDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return newUser;
    }

    @Override
    public void logOut(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id: "+id));
        user.setLoggedIn(false);
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(Long id, LoginRequestDto loginRequestDto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with id: "+id));
        loginRequestDto.setPassword(user.getPassword());
        //we should not update password here. for that we should have a separate method.
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
                    throw new IllegalArgumentException("Invalid field");
            }
        });
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId){
//        fetch providertype and providerId from oAuth2User and save it in our user.
        AuthProviderType providerType = jwtUtil.getProviderTypeFromRegistrationId(registrationId);

        String providerId = jwtUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);
//        determine email or username from oAuth2User
        String email = jwtUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
        String name = oAuth2User.getAttribute("name");

//        check if user already exists with this providerType and providerId
        User userByOauth2 = userRepository.findByProviderTypeAndProviderId(providerType,providerId).orElse(null);

//      check if user already exists with this email in our db
        User userByEmail = userRepository.findByEmail(email).orElse(null);
        if(userByOauth2 == null && userByEmail == null){
//            send to signup flow as this user don't exist in our db
            userByOauth2 = signUpInternal(new SignUpRequestDto(name,email, null),providerType,providerId);

        }

        if(userByEmail != null){
            throw new BadCredentialsException("User already exists with this email "+email);
        }

        LoginResponseDto loginResponseDto = new LoginResponseDto(jwtUtil.generateToken(userByOauth2),userByOauth2.getId());
        userByOauth2.setLoggedIn(true);
        return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);

    }
}
