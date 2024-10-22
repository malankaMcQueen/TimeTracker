package com.example.TimeTracker.controller;


import com.example.TimeTracker.dto.NewProjectDTO;
import com.example.TimeTracker.dto.ProjectUsersAction;
import com.example.TimeTracker.dto.ProjectUsersDTO;
import com.example.TimeTracker.model.Project;
import com.example.TimeTracker.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {

    private ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<Project> createNewProject(@RequestBody NewProjectDTO newProject) {
        return new ResponseEntity<>(projectService.createNewProject(newProject), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Project>> getAllProjects() {
        return new ResponseEntity<>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @PatchMapping("/{projectId}/users")
    public ResponseEntity<Project> modifyProjectUsers(@PathVariable Long projectId,
                                                      @RequestBody ProjectUsersDTO projectUsersDTO) {
        Project project = null;
        if (projectUsersDTO.getAction().equals(ProjectUsersAction.ADD_USER)) {
            project = projectService.addUserToProject(projectId, projectUsersDTO);
        }
        else if (projectUsersDTO.getAction().equals(ProjectUsersAction.REMOVE_USER)) {
            project = projectService.removeUserToProject(projectId, projectUsersDTO);
        }
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
