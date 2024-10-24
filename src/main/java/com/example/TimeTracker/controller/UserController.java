package com.example.TimeTracker.controller;


import com.example.TimeTracker.dto.UserChangePasswordDTO;
import com.example.TimeTracker.model.Role;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.UserRepository;
import com.example.TimeTracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;
    // Получить всех пользователей
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
    // Изменить роль юзеру
    @PutMapping("/changeRole")
    public ResponseEntity<User> changeRole(@RequestParam String userEmail, @RequestParam Role newRole) {
        return new ResponseEntity<>(userService.changeRole(userEmail, newRole), HttpStatus.OK);
    }

    // Удалить пользователя
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        userService.deleteUser();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    // Изменить пароль от аккаунта
    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        return new ResponseEntity<>(userService.changePassword(userChangePasswordDTO), HttpStatus.OK);
    }

}
