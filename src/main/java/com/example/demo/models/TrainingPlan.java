package com.example.demo.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainingPlan", orphanRemoval = true)
    @JsonManagedReference
    private List<TrainingSession> trainingSessions;

    public TrainingPlan() {
    }

    // Lets remove user, training plan no longer knows its user
    // now user knows its training plans
    public TrainingPlan(String name, String description, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainingSessions = new ArrayList<>();
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

    // write rest of getters and setters
    public String getDescription() {
        return this.description;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    

}
