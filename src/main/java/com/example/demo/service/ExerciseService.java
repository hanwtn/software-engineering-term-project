package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ExerciseService {
    public static Validation validate(String name) {
        if (name == null || name.isEmpty()) {
            return new Validation(400, "Exercise name must be provided");
        }
        return new Validation();
    }
}
