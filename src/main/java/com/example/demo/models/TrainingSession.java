package com.example.demo.models;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.sql.Time;

@Entity
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tsid;

    @Column
    private String name;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id")
    private TrainingPlan trainingPlan;

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

    public TrainingSession(List<Exercise> exercises, Set<DayOfWeek> dayOfWeeks, Time startTime, Time endTime, String name) {
        this.exercises = new ArrayList<>();
        this.daysOfWeek = new HashSet<>(dayOfWeeks);
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
    
        // Create a copy of each exercise and add it to the training session
        for (Exercise exercise : exercises) {
            Exercise exerciseCopy = new Exercise(
                exercise.getName(),
                exercise.getDescription(),
                exercise.getSets(),
                exercise.getReps(),
                exercise.getIntensity(),
                exercise.getDuration()
            );
            exerciseCopy.setTrainingSession(this);
            this.exercises.add(exerciseCopy);
        }
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
        this.exercises = new ArrayList<>(exercises);
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

    public String getDaysOfWeekAsString() {
    return String.join(", ", this.daysOfWeek.stream().map(Enum::name).collect(Collectors.toList()));
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

    public void setTrainingPlan(TrainingPlan trainingPlan) {
        this.trainingPlan = trainingPlan;
    }

    public TrainingPlan getTrainingPlan() {
        return trainingPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
