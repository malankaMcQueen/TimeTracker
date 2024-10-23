package com.example.TimeTracker.controller;


import com.example.TimeTracker.dto.UserChangePasswordDTO;
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

    @PostMapping("/create")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<User> changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        return new ResponseEntity<>(userService.changePassword(userChangePasswordDTO), HttpStatus.OK);
    }
}
