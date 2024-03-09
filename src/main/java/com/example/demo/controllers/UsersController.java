package com.example.demo.controllers;
// copilot was used during the assingment


import java.util.List;
import java.util.Map;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
// import com.example.quizapp2.models.Users;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepo;

    //simple get request to login page
    @GetMapping("/login")
    public String loginPage() {
        return "users/loginPage";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "users/registerPage";
    }

    @PostMapping("/users/login")
    public String login(@RequestParam Map<String, String> newuser, HttpServletResponse response, Model model)
    {
        System.out.println("Logging in");

        String newUsername = newuser.get("username");
        String newPassword = newuser.get("password");

        // Check if username and password are present
        if (!newuser.containsKey("username") || !newuser.containsKey("password")) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username or password not provided");
            return "users/loginPage";
        }

        User user = userRepo.findByUsername(newUsername);
        if (user == null) {
            response.setStatus(404); // Not Found
            model.addAttribute("error", "Invalid username");
            return "users/loginPage";
        }
        if (!user.getPassword().equals(newPassword)) {
            response.setStatus(401); // Unauthorized
            model.addAttribute("error", "Invalid password");
            return "users/loginPage";
        }
        
        //login successful
        //if 0 go to user page
        if (user.getStatus() == 0) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            return "users/userPage";
        }
        //if 1 go to coach page
        if (user.getStatus() == 1) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            return "users/coachPage";
        }

        //default to login page
        response.setStatus(401); // Unauthorized
        model.addAttribute("error", "Invalid status");
        return "users/loginPage";
    }

    @PostMapping("/users/register")
    public String registerUser(@RequestParam Map<String, String> newuser, HttpServletResponse response, Model model)
    {
        System.out.println("ADD user");
        String newUsername = newuser.get("username");

        //username already exists
        if (userRepo.existsByUsername(newUsername)) {
            System.out.println("Username already exists");
            response.setStatus(409); // Conflict
            model.addAttribute("error", "Username already exists");
            return "redirect:/registerPage";
        }
      
        int newStatus = Integer.parseInt(newuser.getOrDefault("status", "0"));
        String newPassword = newuser.get("password");

        userRepo.save(new User(newUsername, newPassword, newStatus));
        response.setStatus(201);
        model.addAttribute("success", "User created");
        return "redirect:/loginPage";
    }

    
    @PostMapping("/users/deleteAll")
    public String deleteAllUsers(HttpServletResponse response)
    {
    System.out.println("DELETE all users");
    userRepo.deleteAll();
    response.setStatus(204); 
    return "redirect:/users/view";
    }

}

