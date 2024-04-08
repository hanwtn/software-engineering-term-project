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

import com.example.demo.models.TrainingPlan;
import com.example.demo.models.Exercise;
import com.example.demo.models.ExerciseRepository;
import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.TrainingSession;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExerciseController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    private UserService userService = null;

    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private ExerciseRepository exerciseRepo;

    @GetMapping("/exercise/view")
    public String showExercises(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // User is not logged in, redirect to login page
            return "redirect:/login";
        }

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, handle this case appropriately
            session.invalidate(); // Invalidate the session to logout the user
            return "redirect:/login";
        }

        // Fetch exercises added by the logged-in user
        List<Exercise> exercises = user.getExercises();
        model.addAttribute("user", user);
        model.addAttribute("exercises", exercises);

        return "exercises/viewExercise";
    }

    @PostMapping("/exercise/add")
    public String addExercise(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // User is not logged in, redirect to login page
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, handle this case appropriately
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "exercises/addExercise";
    }

    @PostMapping("/exercise/add/submit")
    public String addExerciseSubmit(@RequestParam Map<String, String> newExercise, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        // Check if user is logged in
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, handle this case appropriately
            return "redirect:/login";
        }
        String name = newExercise.get("name");
        String description = newExercise.get("description");
        String intensity = newExercise.get("intensity");
        int sets = Integer.parseInt(newExercise.getOrDefault("sets", "0"));
        int reps = Integer.parseInt(newExercise.getOrDefault("reps", "0"));
        int duration = Integer.parseInt(newExercise.getOrDefault("duration", "0"));

        // Validate the exercise details here (e.g., check if name is empty)

        Exercise exercise = new Exercise(name, description, sets, reps, intensity, duration);

        // Add the new exercise to the user's list of exercises
        user.addExercise(exercise);
        userRepo.save(user);

        model.addAttribute("success", "Exercise added successfully");
        // Add success message to the model to display in the view

        return "redirect:/exercise/view";
    }

    @PostMapping("/exercise/delete")
    public String deleteExercise(@RequestParam("eid") int exerciseId, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            // User is not logged in, redirect to login page
            return "redirect:/login";
        }

        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, redirect to login page
            return "redirect:/login";
        }

        Exercise exercise = exerciseRepo.findById(exerciseId).orElse(null);
        if (exercise == null /* || exercise.getUser().getUid() != userId */) {
            // Exercise not found or does not belong to the user, handle this case
            // appropriately
            model.addAttribute("error", "Exercise not found or not owned by user");
            return "redirect:/exercise/view";
        }

        user.removeExercise(exercise);
        exerciseRepo.delete(exercise);
        userRepo.save(user);

        return "redirect:/exercise/view";
    }

}
