package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseRepository;
import com.example.demo.models.TrainingPlan;
import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.TrainingSession;
import com.example.demo.models.TrainingSessionRepository;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
// import com.example.quizapp2.models.Users;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TrainingSessionController {
    
     @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    @Autowired
    private TrainingSessionRepository trainingSessionRepo;
    @Autowired
    private ExerciseRepository exerciseRepository;
    private UserService userService = null;   


    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/trainingSession/add/submit")
    public String addSession(@RequestParam Map<String, String> newSession, HttpServletResponse response, Model model){

        String name = newSession.get("name");
        String desc = newSession.get("description");

        /* 
        TrainingPlan newTrainingPlan = new TrainingPlan(newName, newDesc, startDate, endDate);
        User user = userRepo.findByUid(userId);
        //add error-check for getting user
        user.addTrainingPlan(newTrainingPlan);
        userRepo.save(user); // Save the user with the new training plan
        //removed user from training plan constructor
        System.out.println("Successfully Added");
        */
        return "redirect:/dashboard";
    }

    @GetMapping("/trainingSession/add")
    public String trainingSessionAdd(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<Exercise> userExercises = exerciseRepository.findByUser(user);
        model.addAttribute("userexercises", userExercises);
        List<Exercise> allExercises = exerciseRepository.findAll();
        model.addAttribute("allexercises", allExercises);

        return "training_sessions/addTrainingSession";
    }

    @GetMapping("/trainingSession/view")
    public String viewTrainingSessionView(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<TrainingPlan> trainingPlans = user.getTrainingPlans();
        model.addAttribute("trainingPlans", trainingPlans);
        return "training_plans/viewTrainingPlan";
    }

    

}
