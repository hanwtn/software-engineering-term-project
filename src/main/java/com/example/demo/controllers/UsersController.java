package com.example.demo.controllers;
// copilot was used during the assingment


import java.util.List;
import java.util.Map;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;
import com.example.demo.models.StudentRepository;
// import com.example.quizapp2.models.Users;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UsersController {

    @Autowired
    private StudentRepository userRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model)
    {
        System.out.println("Getting all users");

        List<User> users = userRepo.findAllByOrderByUid();

        model.addAttribute("students", users);
        return "users/showAll";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response)
    {
        System.out.println("ADD user");
        String newUsername = newuser.get("username");

        if (userRepo.existsByUsername(newUsername)) {
            //redirect it to the add page with an error message
            System.out.println("Username already exists");
            response.setStatus(409); // Conflict
            return "redirect:/users/view";
        }
      

        double newWeight = Double.parseDouble(newuser.getOrDefault("weight", "0.0"));
        double newHeight = Double.parseDouble(newuser.getOrDefault("height", "0.0"));
        int newStatus = Integer.parseInt(newuser.getOrDefault("status", "0"));
        String newPassword = newuser.get("password");

        userRepo.save(new User(newUsername, newWeight, newHeight, newStatus, newPassword));
        response.setStatus(201);
        return "redirect:/users/view";
    }
   
    // @GetMapping("/users/login")
    // {
        

        
    //     //if status is zero go to user page

    //     //if status is one go to coach page
    // }    
    
    @PostMapping("/users/deleteAll")
    public String deleteAllUsers(HttpServletResponse response)
    {
    System.out.println("DELETE all users");
    userRepo.deleteAll();
    response.setStatus(204); 
    return "redirect:/users/view";
    }

}

