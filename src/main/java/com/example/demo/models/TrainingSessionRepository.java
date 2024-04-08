package com.example.demo.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Integer> {
    TrainingSession findBytsid(int tsid);
} 
