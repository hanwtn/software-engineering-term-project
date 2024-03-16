package com.example.demo.models;

import jakarta.persistence.*;

import java.time.LocalDate;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "training_plan_id")
    List<TrainingSession> trainingSessions;

    public TrainingPlan() {

    }

    public TrainingPlan(String name, String description, User user, LocalDate startDate, LocalDate endDate,
            List<TrainingSession> trainingSessions) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trainingSessions = trainingSessions;
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

}
