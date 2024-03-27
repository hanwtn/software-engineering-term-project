package com.example.demo.controllers;



import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.TrainingPlan;
import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }

        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        
        List<TrainingPlan> trainingPlans = trainingPlanRepo.findAll();
        model.addAttribute("trainingPlans", trainingPlans);
        return "admin/adminPage"; 
    }
    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("isAdmin"); 

        return "redirect:/admin/login"; 
    }

    @PostMapping("/admin/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        userRepo.deleteById(userId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute User user) {
        userRepo.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/adminLogin"; 
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        final String adminUsername = "admin";
        final String adminPassword = "276";

        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("isAdmin", true); 
            return "redirect:/admin"; 
        } else {
            redirectAttributes.addFlashAttribute("loginError", "Invalid username or password");
            return "redirect:/admin/login"; 
        }
    }


    @GetMapping("/admin/adminEdit/{userId}")
    public String adminEditAccountForm(@PathVariable("userId") Integer uId, HttpSession session, Model model) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!Boolean.TRUE.equals(isAdmin)) {
            return "redirect:/admin/login";
        }

        Optional<User> userOptional = userRepo.findById(uId);
        if (!userOptional.isPresent()) {
            return "redirect:/admin";
        }

        model.addAttribute("user", userOptional.get());
        return "admin/adminEdit";
    }

    @PostMapping("/admin/adminEdit/{userId}")
    public String adminUpdateAccount(@PathVariable("userId") Integer uId, @RequestParam Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (!Boolean.TRUE.equals(isAdmin)) {
            return "redirect:/admin/login";
        }

        User user = userRepo.findById(uId).orElse(null);
        if (user == null) {
            return "redirect:/admin";
        }

        String newUsername = params.get("username");
        if (!user.getUsername().equals(newUsername) && userRepo.existsByUsername(newUsername)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "redirect:/admin/adminEdit/" + uId;
        }

        user.setUsername(newUsername);
        user.setPassword(params.get("password")); 
        user.setWeight(Double.valueOf(params.get("weight")));
        user.setHeight(Double.valueOf(params.get("height")));
        user.setStatus(Integer.valueOf(params.get("status")));
        userService.saveUser(user);

        return "redirect:/admin";
    }
    @PostMapping("/admin/addUser")
    public String addUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("status") Integer status, @RequestParam("weight") Integer weight, @RequestParam("height") Integer height, RedirectAttributes redirectAttributes) {
        if (userRepo.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "redirect:/admin";
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setStatus(status);
        newUser.setHeight(height);
        newUser.setWeight(weight);
        userRepo.save(newUser);
        redirectAttributes.addFlashAttribute("successMessage", "Account successfully added.");

        return "redirect:/admin";
        
    }

}
