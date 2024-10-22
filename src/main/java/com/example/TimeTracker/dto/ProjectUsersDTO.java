package com.example.TimeTracker.dto;

import lombok.Data;

@Data
public class ProjectUsersDTO {
    Long userId;
    ProjectUsersAction action;
}
