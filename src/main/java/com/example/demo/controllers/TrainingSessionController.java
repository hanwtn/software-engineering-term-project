package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
import jakarta.servlet.http.HttpSession;

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
    public String addSession(@RequestParam Map<String, String> newSession,
            @RequestParam("tpid") int tpid,
            @RequestParam(value = "exercises", required = false) String[] selectedExercises,
            HttpServletResponse response, Model model) {

        String name = newSession.get("name");
        List<Exercise> exercises = new ArrayList<>();
        System.out.println("Training add submit called");
        if (selectedExercises != null) {
            for (String exerciseId : selectedExercises) {
                System.out.println("Selected Exercise ID: " + exerciseId);
                Exercise exercise = exerciseRepository.findByEid(Integer.parseInt(exerciseId));
                exercises.add(exercise);
                // Log the details of the fetched exercise
                System.out.println("Fetched Exercise: " + exercise.getName() + ", Sets: " + exercise.getSets() + ", Reps: " + exercise.getReps());
            }
        }

        // Default for now 
        Time startTime = Time.valueOf("09:00:00");
        Time endTime = Time.valueOf("10:00:00");

        

        // HardCoded for now :3
        TrainingPlan trainingPlan = trainingPlanRepo.findBytpid(tpid);

        Set<DayOfWeek> daysOfWeek = Arrays.stream(newSession.get("daysOfWeek").split(","))
                                       .map(DayOfWeek::valueOf)
                                       .collect(Collectors.toSet());

        // Check for overlapping days
        for (TrainingSession session : trainingPlan.getTrainingSessions()) {
            Set<DayOfWeek> existingDays = session.getDaysOfWeek();
            if (!Collections.disjoint(daysOfWeek, existingDays)) {
                model.addAttribute("error", "Overlapping days with existing sessions");
                return "redirect:/trainingSession/add";
            }
        }

        TrainingSession newTrainingSession = new TrainingSession(exercises, daysOfWeek, startTime, endTime, name);

        trainingPlan.addTrainingSession(newTrainingSession);
        trainingPlanRepo.save(trainingPlan);
        response.setStatus(200); // OK
        Integer userId = Integer.parseInt(newSession.get("userId"));
        return "redirect:/trainingPlan/viewAll?userId=" + userId;
    }

    @GetMapping("/trainingSession/add")
    public String trainingSessionAdd(@RequestParam("tpid") int tpid, HttpSession session,
            HttpServletResponse response,
            Model model) {
        int userId = (int) session.getAttribute("userId");
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        model.addAttribute("tpid", tpid);
        String search = "";
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
        return "training_sessions/addTrainingSession";
    }

    @GetMapping("/trainingSession/add/search")
    public String trainingSessionSearch(@RequestParam Map<String, String> newUser, HttpServletResponse response,
            Model model) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        String search = newUser.get("search");
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
        return "training_sessions/addTrainingSession";
    }

    @GetMapping("/trainingSession/view")
    public String viewTrainingSessionView(@RequestParam Map<String, String> newSession, HttpServletResponse response,
            Model model) {
        List<TrainingSession> trainingSessions = trainingSessionRepo.findAll();
        model.addAttribute("trainingSessions", trainingSessions);
        System.out.println("HERE");
        System.out.println(trainingSessions.get(0).getName());
        return "training_sessions/viewTrainingSession";
    }

    @PostMapping("/trainingSession/delete")
    public String deleteTrainingSession(@RequestParam Map<String, String> newSession, @RequestParam("tsid") int tsid, Model model) {
        TrainingSession ts = trainingSessionRepo.findBytsid(tsid);

        if (ts == null) {
            model.addAttribute("error", "Training session not found");
            Integer userId = Integer.parseInt(newSession.get("userId"));
            return "redirect:/trainingPlan/viewAll?userId=" + userId;// fix it
        }

        trainingSessionRepo.delete(ts);
        Integer userId = Integer.parseInt(newSession.get("userId"));
        return "redirect:/trainingPlan/viewAll?userId=" + userId;
    }

}
