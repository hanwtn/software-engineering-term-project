package com.example.demo.models;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan,Integer> {
    ArrayList<TrainingPlan> getAllTrainingPlansByUser(User user);
    TrainingPlan findByTpid(int tpid);

} 
