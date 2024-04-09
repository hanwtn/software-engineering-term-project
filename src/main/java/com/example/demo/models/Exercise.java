package com.example.demo.models;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;

    @Column
    private String name;

    @Column(nullable = true)
    private int sets;

    @Column(nullable = true)
    private int reps;

    @Column(nullable = true)
    private String intensity;

    @Column(nullable = true)
    private int duration;

    @Column(nullable = true)
    private String description;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "training_session_id")
    // private TrainingSession trainingSession;

    @ManyToMany(mappedBy = "exercises")
    private List<TrainingSession> trainingSessions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Exercise() {
    }

    public Exercise(String name, String description, int sets, int reps, String intensity, int duration) {
        this.name = name;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
        this.intensity = intensity;
        this.duration = duration;
    }
    
    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }


    public void addTrainingSession(TrainingSession session) {
        if (!trainingSessions.contains(session)) {
            trainingSessions.add(session);
            session.getExercises().add(this); 
        }
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public int getEid() {
        return eid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}