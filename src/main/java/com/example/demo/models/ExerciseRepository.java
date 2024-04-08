package com.example.demo.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    Exercise findByEid(int Exerciseid);
    List<Exercise> findByUser(User user);
    List<Exercise> findByNameIgnoreCaseContaining(String search);
}