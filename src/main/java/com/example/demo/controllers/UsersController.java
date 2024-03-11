package com.example.demo.controllers;

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
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
    private final int USERNAME_MIN_LENGTH = 4;
    private final int PASSWORD_MIN_LENGTH = 8;

    @Autowired
    private UserRepository userRepo;

    // simple get request to login page
    @GetMapping("/login")
    public String loginPage() {
        return "users/loginPage";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "users/registerPage";
    }

    @GetMapping("/users/logout")
    public String logout(HttpSession session) {
        System.out.println("Logging out");
        session.invalidate(); // Invalidate the session to logout the user
        return "users/loginPage";
    }

    @PostMapping("/users/login")
    public String login(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        System.out.println("Logging in");

        String newUsername = newUser.get("username");
        String newPassword = newUser.get("password");

        // Check if username and password are present
        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username or password not provided");
            return "users/loginPage";
        }

        // Check if username reaches minimum length
        if (newUsername.length() < USERNAME_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
            return "users/loginPage";
        }

        // Check if password reaches minimum length
        if (newPassword.length() < PASSWORD_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
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

        // login successful
        // if 0 go to user page
        if (user.getStatus() == 0) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            return "users/userPage";
        }

        // TODO: (low priority) Reconsider/remove Coach as it is no longer used
        // if 1 go to coach page
        if (user.getStatus() == 1) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            return "users/coachPage";
        }

        // default to login page
        response.setStatus(401); // Unauthorized
        model.addAttribute("error", "Invalid status");
        return "users/loginPage";
    }

    @PostMapping("/users/register")
    public String registerUser(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        System.out.println("ADD user");

        String newUsername = newUser.get("username");
        int newStatus = Integer.parseInt(newUser.getOrDefault("status", "0"));
        String newPassword = newUser.get("password");

        // Check if username and password are present
        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username or password not provided");
            return "users/registerPage";
        }

        // Check if username reaches minimum length
        if (newUsername.length() < USERNAME_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
            return "users/registerPage";
        }

        // Check if password reaches minimum length
        if (newPassword.length() < PASSWORD_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
            return "users/registerPage";
        }

        // username already exists
        if (userRepo.existsByUsername(newUsername)) {
            System.out.println("Username already exists");
            response.setStatus(409); // Conflict
            model.addAttribute("error", "Username already exists");
            return "users/registerPage";
        }

        userRepo.save(new User(newUsername, newPassword, newStatus));
        response.setStatus(201);
        model.addAttribute("success", "User created");
        return "users/loginPage";
    }

    @PostMapping("/users/deleteAll")
    public String deleteAllUsers(HttpServletResponse response) {
        System.out.println("DELETE all users");
        userRepo.deleteAll();
        response.setStatus(204);
        return "redirect:/users/view";
    }
}
