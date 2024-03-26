package com.example.demo.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TrainingPlan")
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tpid;

    @Column
    private String name;
    @Column(nullable = true)
    private String description;
    @Column
    private LocalDate startDate;
    @Column(nullable = true)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingPlan", orphanRemoval = true)
    private List<TrainingSession> trainingSessions;

    public TrainingPlan() {

    }

    // Lets remove user, training plan no longer knows its user
    //now user knows its training plans
    public TrainingPlan(String name, String description, LocalDate startDate, LocalDate endDate, List<TrainingSession> ts) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainingSessions = ts;
    }

    public int getId() {
        return tpid;
    }

    public void setId(int tpid) {
        this.tpid = tpid;
    }

    public String getName() {
        return this.name;
    }

    public int getTpid() {
        return tpid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    //write rest of getters and setters
    public String getDescription() {
        return this.description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

     public void addTrainingSession(TrainingSession trainingSession) {
        if (trainingSessions == null) {
            trainingSessions = new ArrayList<>();
        }
        trainingSessions.add(trainingSession);
        trainingSession.setTrainingPlan(this);
    }

    public List<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSession> sessions) {
        this.trainingSessions = sessions;
    }




}
