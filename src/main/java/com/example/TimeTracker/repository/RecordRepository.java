package com.example.TimeTracker.repository;

import com.example.TimeTracker.model.Project;
import com.example.TimeTracker.model.Record;
import com.example.TimeTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
//    List<Record> findByUserAndProjectAndEndTimeIsNull(User user, Project project);
    Optional<Record> findByUserAndProjectAndEndTimeIsNull(User user, Project project);
    Optional<Record> findByUserAndEndTimeIsNull(User user);

    List<Record> findByUserAndProject(User user, Project project);
}
