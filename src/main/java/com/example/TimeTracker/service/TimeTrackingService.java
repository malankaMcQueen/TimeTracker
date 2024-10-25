package com.example.TimeTracker.service;

import com.example.TimeTracker.exception.BadRequestException;
import com.example.TimeTracker.exception.ResourceNotFoundException;
import com.example.TimeTracker.model.Project;
import com.example.TimeTracker.model.Record;
import com.example.TimeTracker.model.User;
import com.example.TimeTracker.repository.ProjectRepository;
import com.example.TimeTracker.repository.RecordRepository;
import com.example.TimeTracker.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TimeTrackingService {
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private RecordRepository recordRepository;

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public void deleteRecord(Long recordId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    recordRepository.deleteById(recordId);
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Illegal access");
    }

    // Начать трекинг времени
    public Record startTracking(String projectName) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName(); // Получаю емайл из контекста
        User user = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new ResourceNotFoundException("User with email: " + userEmail + " doesn't exist"));
        // Поиск записи в которой время конца = null
        Optional<Record> openRecords = recordRepository.findByUserAndEndTimeIsNull(user);
        if (openRecords.isPresent()) {
            throw new IllegalStateException("There is already an open time record for this user.");
        }
        Project project = projectRepository.findByName(projectName).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectName + " doesn't exist"));
        // Проверка установлен ли пользователь на этот проект
        if (!project.getUsers().contains(user)) {
            throw new BadRequestException("User dont set on this project");
        }
        Record record = new Record();
        record.setUser(user);
        record.setProject(project);
        record.setStartTime(LocalDateTime.now());
        record.setEndTime(null);
        return recordRepository.save(record);
    }
    // Остановить трекинг
    public Record stopTracking(String projectName) {
        // Получить емайл из контекста
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Project project = projectRepository.findByName(projectName).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectName + " doesn't exist"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new ResourceNotFoundException("User with email: " + userEmail + " doesn't exist"));
        // Поиск записи в которой время конца = null
        Record record = recordRepository.findByUserAndProjectAndEndTimeIsNull(user, project)
                .orElseThrow(() -> new IllegalStateException("No active time record found for this project and user"));
        record.setEndTime(LocalDateTime.now());
        return recordRepository.save(record);
    }

    public Duration getTotalTimeSpent(String projectName, String userEmail) {
        Project project = projectRepository.findByName(projectName).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectName + " doesn't exist"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new ResourceNotFoundException("User with email: " + userEmail + " doesn't exist"));
        List<Record> records = recordRepository.findByUserAndProject(user, project);
        // Суммируем разницу между startTime и endTime для всех записей
        Duration totalTime = Duration.ZERO;
        for (Record record : records) {
            // Если время конца не установлено, беру текущее
            if (record.getEndTime() == null) {
                record.setEndTime(LocalDateTime.now());
            }
            Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
            totalTime = totalTime.plus(duration);
        }
        return totalTime;
    }
}
