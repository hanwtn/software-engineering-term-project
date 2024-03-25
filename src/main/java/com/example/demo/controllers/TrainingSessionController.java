package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String addSession(@RequestParam Map<String, String> newSession, @RequestParam(value = "exercises", required = false) String[] selectedExercises,  HttpServletResponse response, Model model){

        String desc = newSession.get("description");
        List<Exercise> exercises = new ArrayList<>();;
        if (selectedExercises != null) {
            for (String exerciseId : selectedExercises) {
                System.out.println(exerciseId);
                exercises.add(exerciseRepository.findByEid(Integer.parseInt(exerciseId)));
            }
        }
        Set<DayOfWeek> dayOfWeeks = new HashSet<>();
        dayOfWeeks.add(DayOfWeek.WEDNESDAY);
        dayOfWeeks.add(DayOfWeek.SATURDAY);
        Time startTime = Time.valueOf("09:00:00");
        Time endTime = Time.valueOf("10:00:00");

        TrainingSession newTrainingSession = new TrainingSession(exercises, dayOfWeeks, startTime, endTime);

        TrainingPlan trainingPlan = trainingPlanRepo.findBytpid(17);

        trainingPlan.addTrainingSession(newTrainingSession);
        trainingPlanRepo.save(trainingPlan);
        return "redirect:/dashboard";
    }

    @GetMapping("/trainingSession/add")
    public String trainingSessionAdd(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model ) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        String search = "";
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
        return "training_sessions/addTrainingSession";
    }

    @GetMapping("/trainingSession/add/search")
    public String trainingSessionSearch(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model ) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        String search = newUser.get("search");
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
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
