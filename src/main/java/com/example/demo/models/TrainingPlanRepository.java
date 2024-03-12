package com.example.demo.models;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    boolean existsByid(int id);
}
