package com.example.TimeTracker.service;

import com.example.TimeTracker.exception.BadRequestException;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.TimeTracker.dto.UserChangePasswordDTO;
import com.example.TimeTracker.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createNewUser(User user) {
        // Шифрую пароль перед сохранением
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("User already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Получение всех пользователей
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Удаление пользователя
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id: " + userId + " doesn't exist"));
        userRepository.delete(user);
    }

    // Смена пароля пользователя
    public User changePassword(UserChangePasswordDTO userChangePasswordDTO) {
        if (userChangePasswordDTO.getOldPassword().equals(userChangePasswordDTO.getNewPassword())) {
            throw new BadRequestException("Old and new password equals");
        }
        User user = userRepository.findByEmail(userChangePasswordDTO.getUserEmail()).orElseThrow(()
                -> new ResourceNotFoundException("User with email: "+ userChangePasswordDTO.getUserEmail() + " doesn't exist"));
        // Проверка старого пароля
        if (!passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        // Установка нового пароля
        user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
        return userRepository.save(user);
    }
}

