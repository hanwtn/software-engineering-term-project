package com.example.demo.service;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TrainingSessionService {
    public static Validation validate(String name, Set<DayOfWeek> daysOfWeek, Time startTime, Time endTime) {
        // name is empty
        if (name.isEmpty()) {
            return new Validation(400, "Training session name must be provided.");
        }
        // No day of the week chosen
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            return new Validation(400, "You must choose at least one day of the week for this training session.");
        }
        // start time not before end time
        if (!startTime.before(endTime)) {
            return new Validation(400, "Start time must be before end time.");
        }
        return new Validation();
    }
}
