package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.TrainingPlanRepository;

@Service
public class TrainingPlanService {
    
    @Autowired
    private TrainingPlanRepository trainingPlanRepository;

    public boolean checkForUserTrainingPlan(int id) {
        return trainingPlanRepository.existsByid(id);
    }
}
