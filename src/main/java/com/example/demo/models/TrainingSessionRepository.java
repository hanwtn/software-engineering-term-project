package com.example.demo.models;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Integer> {
    TrainingSession findBytsid(int tsid);
} 
