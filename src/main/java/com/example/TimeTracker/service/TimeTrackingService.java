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

    public Record createNewRecord(Record record) {
        return null;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public void deleteRecord(Long recordId) {
        recordRepository.deleteById(recordId);
    }

    public Record startTracking(String projectName, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new ResourceNotFoundException("User with email: " + userEmail + " doesn't exist"));
        Optional<Record> openRecords = recordRepository.findByUserAndEndTimeIsNull(user);
        if (openRecords.isPresent()) {
            throw new IllegalStateException("There is already an open time record for this user.");
        }
        Project project = projectRepository.findByName(projectName).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectName + " doesn't exist"));
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

    public Record stopTracking(String projectName, String userEmail) {
        Project project = projectRepository.findByName(projectName).orElseThrow(()
                -> new ResourceNotFoundException("Project: " + projectName + " doesn't exist"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(()
                -> new ResourceNotFoundException("User with email: " + userEmail + " doesn't exist"));
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
            if (record.getEndTime() == null) {
                record.setEndTime(LocalDateTime.now());
            }
            Duration duration = Duration.between(record.getStartTime(), record.getEndTime());
            totalTime = totalTime.plus(duration);
        }
        return totalTime;
    }
}
