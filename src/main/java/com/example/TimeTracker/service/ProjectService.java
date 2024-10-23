package com.example.TimeTracker.service;

import com.example.TimeTracker.dto.NewProjectDTO;
import com.example.TimeTracker.dto.ProjectUsersDTO;
import com.example.TimeTracker.exception.BadRequestException;
import com.example.TimeTracker.exception.ResourceNotFoundException;
import com.example.TimeTracker.model.Project;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.ProjectRepository;
import com.example.TimeTracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    public Project createNewProject(NewProjectDTO newProjectDTO) {
        if (projectRepository.findByName(newProjectDTO.getName()).isPresent()) {
            throw new BadRequestException("Project already exist");
        }
        Project project = new Project();
        project.setName(newProjectDTO.getName());
        project.setUsers(new ArrayList<>());
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project addUserToProject(ProjectUsersDTO projectUsersDTO) {
        Project project = projectRepository.findByName(projectUsersDTO.getProjectName()).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectUsersDTO.getProjectName() + "doesnt exist"));
        User user = userRepository.findByEmail(projectUsersDTO.getUserEmail()).orElseThrow(()
                -> new ResourceNotFoundException("User with id: " + projectUsersDTO.getUserEmail() + "doesnt exist"));
        if (!project.getUsers().contains(user)) {
            project.getUsers().add(user);
            projectRepository.save(project);
        }
        return project;
    }

    public Project removeUserToProject(ProjectUsersDTO projectUsersDTO) {
        Project project = projectRepository.findByName(projectUsersDTO.getProjectName()).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectUsersDTO.getProjectName() + "doesnt exist"));
        User user = userRepository.findByEmail(projectUsersDTO.getUserEmail()).orElseThrow(()
                -> new ResourceNotFoundException("User with id: " + projectUsersDTO.getUserEmail() + "doesnt exist"));
        project.getUsers().remove(user);
        return projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
