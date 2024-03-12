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

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public TrainingPlan() {

    }

    public TrainingPlan(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.startDate = LocalDate.of(2022, 3, 11);
        this.endDate = LocalDate.of(2022, 3, 16);
        this.user = user;
    }

    public int getId() {
        return tpid;
    }

    public void setId(int tpid) {
        this.tpid = tpid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return this.name;
    }
    
}
