    package com.example.TimeTracker.service;

    import com.example.TimeTracker.aspect.AspectAnnotation;
    import com.example.TimeTracker.exception.BadRequestException;
    import com.example.TimeTracker.model.Role;
    import com.example.TimeTracker.model.User;
    import com.example.TimeTracker.repository.UserRepository;
    import lombok.AllArgsConstructor;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.stereotype.Service;
    import java.util.List;
    import com.example.TimeTracker.dto.UserChangePasswordDTO;
    import com.example.TimeTracker.exception.ResourceNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    @AllArgsConstructor
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        // Получение всех пользователей
        public List<User> getAll() {
            return userRepository.findAll();
        }

        // Удаление пользователя
        @Transactional
        public void deleteUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userRepository.deleteByEmail(authentication.getName());
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

        public User changeRole(String userEmail, Role newRole) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(()
                    -> new ResourceNotFoundException("User with email: "+ userEmail + " doesn't exist"));
            if (!user.getRole().equals(newRole)) {
                user.setRole(newRole);
                userRepository.save(user);
            }
            return user;
        }
    }

