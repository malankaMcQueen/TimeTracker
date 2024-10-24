package com.example.TimeTracker.configuration;

import com.example.TimeTracker.model.Role;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeAdmin() {
        return args -> {
            String adminEmail = "admin";
            String adminPassword = "admin";

            // Проверка, существует ли пользователь с ролью ADMIN
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ROLE_ADMIN); // Устанавливаем роль ADMIN
                userRepository.save(admin);
                System.out.println("Admin account created: " + adminEmail);
            } else {
                System.out.println("Admin account already exists.");
            }
        };
    }
}

