package com.example.TimeTracker.dto;

import lombok.Data;

@Data
public class ProjectUsersDTO {
    private String projectName;
    private String userEmail;
    private ProjectUsersAction action;
}
