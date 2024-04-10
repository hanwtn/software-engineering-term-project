package com.example.demo.controllers;

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
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
import com.example.demo.service.ExerciseService;
import com.example.demo.service.Validation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExerciseController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExerciseRepository exerciseRepo;

    @GetMapping("/exercise/view")
    public String showExercises(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Integer userId = (Integer) session.getAttribute("userId");
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
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, handle this case appropriately
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "exercises/addExercise";
    }

    @PostMapping("/exercise/add/submit")
    public String addExerciseSubmit(@RequestParam Map<String, String> newExercise, HttpSession session,
            HttpServletResponse response, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            // User not found, handle this case appropriately
            return "redirect:/login";
        }
        String name = newExercise.get("name");
        String description = newExercise.get("description");
        String intensity = newExercise.get("intensity");
        int sets = 0;
        String setsStr = newExercise.getOrDefault("sets", "0");
        if (!setsStr.equals("")) {
            sets = Integer.parseInt(setsStr);
        }
        int reps = 0;
        String repsStr = newExercise.getOrDefault("reps", "0");
        if (!setsStr.equals("")) {
            reps = Integer.parseInt(repsStr);
        }
        int duration = 0;
        String durationStr = newExercise.getOrDefault("duration", "0");
        if (!durationStr.equals("")) {
            duration = Integer.parseInt(durationStr);
        }

        // Validate the exercise details here (e.g., check if name is empty)
        Validation validate = ExerciseService.validate(name);
        if (validate.isError) {
            response.setStatus(validate.status);
            model.addAttribute("error", validate.message);
            model.addAttribute("name", name);
            model.addAttribute("description", description);
            model.addAttribute("intensity", intensity);
            model.addAttribute("sets", sets);
            model.addAttribute("reps", reps);
            model.addAttribute("duration", duration);
            return "exercises/addExercise";
        }

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
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Integer userId = (Integer) session.getAttribute("userId");
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
