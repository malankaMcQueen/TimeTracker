package com.example.TimeTracker.service;

import com.example.TimeTracker.dto.AuthRequest;
import com.example.TimeTracker.exception.ResourceNotFoundException;
import com.example.TimeTracker.exception.UserAlreadyExistException;
import com.example.TimeTracker.model.Role;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;

    public String login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }

    public void register(AuthRequest authRequest) {
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }

        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRole(Role.ROLE_USER); // Назначаем роль по умолчанию

        userRepository.save(user);
    }

}

