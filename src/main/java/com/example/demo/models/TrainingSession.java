package com.example.demo.models;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Time;

@Entity
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tsid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "training_session_id")
    private List<Exercise> exercises; // list is ordered
    // may add time to the training session

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek;

    @Column
    private Time startTime;

    @Column
    private Time endTime;

    public TrainingSession() {
        this.exercises = new ArrayList<>();
        this.daysOfWeek = new HashSet<>();
    }

    public TrainingSession(List<Exercise> exercises, Set<DayOfWeek> dayOfWeeks, Time startTime, Time endTime) {
        this.exercises = new ArrayList<>(exercises);
        this.daysOfWeek = new HashSet<>(dayOfWeeks);
        this.startTime = startTime;
        this.endTime = endTime;
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
                exercise.getIntensity(),
                exercise.getDuration());
        this.exercises.add(exerciseCopy);
    }

    // TO DO: not sure if this is the right way to do this
    // From what I know, orphanRemoval=true will automatically remove the Exercise
    // from the database when we
    // remove it from the List<Exercise>
    public void removeExerciseById(int eIdToRemove) {
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).getEid() == eIdToRemove) {
                exercises.remove(i);
            }
        }
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
