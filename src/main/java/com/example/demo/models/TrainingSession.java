package com.example.demo.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tsid;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private List<Exercise> exercises; //list is ordered
    //may add time to the training session

    public TrainingSession() {
        this.exercises = new ArrayList<>();
    }

    public TrainingSession(List<Exercise> exercises) {
        this.exercises = new ArrayList<>(exercises);
    }

    public int getTsid() {
        return tsid;
    }

    public void setTsid(int tsid) {
        this.tsid = tsid;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        // Create a copy of the exercise and add it to the list
        Exercise exerciseCopy = new Exercise(
            exercise.getName(),
            exercise.getDescription(),
            exercise.getSets(),
            exercise.getReps(),
            exercise.getIntensity()
        );
        this.exercises.add(exerciseCopy);
    }
}
