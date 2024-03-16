package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.UserRepository;
import com.example.demo.service.UserService;

public class TrainingPlanController {
    
     @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    private UserService userService = null;
    

    
}
