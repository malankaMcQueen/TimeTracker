package com.example.TimeTracker.controller;

import com.example.TimeTracker.model.Record;
import com.example.TimeTracker.service.TimeTrackingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/timeTracking")
public class TimeTrackingController {

    private TimeTrackingService timeTrackingService;

    @PostMapping("/start")
    public ResponseEntity<Record> startTracking(@RequestParam String projectName, @RequestParam String userEmail) {
        return new ResponseEntity<>(timeTrackingService.startTracking(projectName, userEmail), HttpStatus.CREATED);
    }

    @PutMapping("/stop")
    public ResponseEntity<Record> stropTracking(@RequestParam String projectName, @RequestParam String userEmail) {
        return new ResponseEntity<>(timeTrackingService.stopTracking(projectName, userEmail), HttpStatus.OK);
    }

    @GetMapping("/totalTime")
    public ResponseEntity<Duration> getTotalTimeSpent(@RequestParam String projectName, @RequestParam String userEmail) {
        Duration totalTime = timeTrackingService.getTotalTimeSpent(projectName, userEmail);
        return new ResponseEntity<>(totalTime, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Record>> getAllRecords() {
        return new ResponseEntity<>(timeTrackingService.getAllRecords(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId) {
        timeTrackingService.deleteRecord(recordId);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

//    @PutMapping("/changePassword")
//    public ResponseEntity<String> changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO) {
//        return new ResponseEntity<>(userService.changePassword(), HttpStatus.OK);
//    }

}
