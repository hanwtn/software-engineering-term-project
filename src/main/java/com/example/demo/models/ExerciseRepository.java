package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findByEid(int Exerciseid);

    List<Exercise> findByNameIgnoreCaseContaining(String search);
}