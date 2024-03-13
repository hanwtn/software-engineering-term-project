package com.example.demo.models;


import jakarta.persistence.*;
import com.example.demo.models.User;

@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
}
