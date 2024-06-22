package com.cristin.simpleauth.service;

import com.cristin.simpleauth.dto.LoginDto;
import com.cristin.simpleauth.dto.SignUpDto;
import com.cristin.simpleauth.entity.Role;
import com.cristin.simpleauth.entity.User;
import com.cristin.simpleauth.repository.RoleRepository;
import com.cristin.simpleauth.repository.UserRepository;
import com.cristin.simpleauth.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public String registerUser(SignUpDto signUpDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return "Username is already taken!";
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return "Email is already taken!";
        }

        // Create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Set roles (assuming 'admin' role exists in the database)
        Role role = roleRepository.findByName("admin")
                .orElseThrow(() -> new RuntimeException("Role not found")); // Handle appropriately
        user.setRoles(Collections.singleton(role));

        // Save user to database
        userRepository.save(user);

        return "User registered successfully";
    }

    public String authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }
}
