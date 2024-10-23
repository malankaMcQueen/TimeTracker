package com.example.TimeTracker.dto;

import lombok.Data;

@Data
public class UserChangePasswordDTO {
    private String userEmail;
    private String oldPassword;
    private String newPassword;
}
