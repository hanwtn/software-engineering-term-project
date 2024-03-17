package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    @Column(unique = true)
    private String username;

    @Column(nullable = true)
    private Double weight;

    @Column(nullable = true)
    private Double height;

    private Integer status; // 0 = regular user, 1 = coach, 2 = admin

    private String password;

    // TO DO: do this better
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingPlan> trainingPlans = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises;

    //make method to add training plan
    // each user holds a list of training plans
    public void addTrainingPlan(TrainingPlan trainingPlan) {
        if (trainingPlans == null) {
            trainingPlans = new ArrayList<>();
        }
        trainingPlans.add(trainingPlan);
        trainingPlan.setUser(this);
    }

    public void removeTrainingPlan(TrainingPlan trainingPlan) {
        if (trainingPlans != null) {
            trainingPlans.remove(trainingPlan);
            trainingPlan.setUser(null);
            //do that in controller
            // trainingPlanRepository.delete(trainingPlan);
        }
    }

    
    public List<TrainingPlan> getTrainingPlans() {
        return trainingPlans;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public User() {
    }

    public User(String username, String password, Integer status) {
        this.username = username;
        this.status = status;
        this.password = password;
        this.weight = 0.0;
        this.height = 0.0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getWeight() {
        return weight != null ? weight : 0.0;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height != null ? height : 0.0;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
