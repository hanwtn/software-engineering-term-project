package com.example.demo.models;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan,Integer> {
    ArrayList<TrainingPlan> getAllTrainingPlansByUser(User user);
} 
