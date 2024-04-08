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
import com.example.demo.service.TrainingSessionService;
import com.example.demo.service.Validation;

import io.micrometer.core.ipc.http.HttpSender.Response;

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
    @Autowired
    private UserService userService;

    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/trainingSession/add")
    public String trainingSessionAdd(@RequestParam("tpid") int tpid, HttpSession session,
            HttpServletResponse response,
            Model model) {
        model.addAttribute("tpid", tpid);
        String search = "";
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
        return "training_sessions/addTrainingSession";
    }

    @PostMapping("/trainingSession/add/submit")
    public String addSession(@RequestParam Map<String, String> newSession,
            @RequestParam("tpid") int tpid,
            @RequestParam(value = "exercises", required = false) String[] selectedExercises,
            HttpServletResponse response, Model model,
            @RequestParam("startTime") String startTimeString,
            @RequestParam("endTime") String endTimeString,
            @RequestParam(value = "daysOfWeek", required = false) String[] daysOfWeekArray) {

        Time startTime = Time.valueOf(startTimeString);
        Time endTime = Time.valueOf(endTimeString);
        String name = newSession.get("name");
        List<Exercise> exercises = new ArrayList<>();
        Set<DayOfWeek> daysOfWeek;
        if (daysOfWeekArray != null) {
            daysOfWeek = Arrays.stream(daysOfWeekArray).map(String::toUpperCase).map(DayOfWeek::valueOf)
                    .collect(Collectors.toSet());
        } else {
            daysOfWeek = null;
        }

        Validation validate = TrainingSessionService.validate(name, daysOfWeek, startTime, endTime);
        if (validate.isError) {
            response.setStatus(validate.status);
            model.addAttribute("error", validate.message);
            model.addAttribute("name", name);
            if (daysOfWeekArray != null) {
                for (String day : daysOfWeekArray) {
                    model.addAttribute(day, true);
                }
            }
            model.addAttribute("tpid", tpid);
            String search = "";
            List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
            model.addAttribute("exercises", allExercises);
            return "training_sessions/addTrainingSession";
        }

        System.out.println("Training add submit called");
        if (selectedExercises != null) {
            for (String exerciseId : selectedExercises) {
                System.out.println("Selected Exercise ID: " + exerciseId);
                Exercise exercise = exerciseRepository.findByEid(Integer.parseInt(exerciseId));
                exercises.add(exercise);
                // Log the details of the fetched exercise
                System.out.println("Fetched Exercise: " + exercise.getName() + ", Sets: " + exercise.getSets()
                        + ", Reps: " + exercise.getReps());
            }
        }

        // HardCoded for now :3
        TrainingPlan trainingPlan = trainingPlanRepo.findByTpid(tpid);

        // Check for overlapping days
        // boolean overlapFound = false;
        // for (TrainingSession session : trainingPlan.getTrainingSessions()) {
        // Set<DayOfWeek> existingDays = session.getDaysOfWeek();
        // if (!Collections.disjoint(daysOfWeek, existingDays)) {
        // model.addAttribute("error", "Overlapping days with existing sessions. Please
        // select non-overlapping days.");
        // overlapFound = true;
        // break; // No need to check further if overlap is found
        // }
        // }

        // if (overlapFound) {
        // // If overlap is found, return to the form with error messages and preserve
        // user input
        // model.addAttribute("trainingSession", newSession); // Consider creating a DTO
        // class for better structure
        // model.addAttribute("selectedExercises", selectedExercises); // Add other
        // necessary attributes similarly
        // model.addAttribute("startTime", startTimeString);
        // model.addAttribute("tpid", tpid);
        // model.addAttribute("endTime", endTimeString);
        // model.addAttribute("daysOfWeek", daysOfWeekArray); // You may need to handle
        // conversion for display
        // // Load additional necessary data for the form
        // // e.g., available exercises, etc., that are needed to render the form
        // properly
        // return "training_sessions/addTrainingSession"; // Return the view name of the
        // form
        // }

        TrainingSession newTrainingSession = new TrainingSession(exercises, daysOfWeek, startTime, endTime, name);

        trainingPlan.addTrainingSession(newTrainingSession);
        trainingPlanRepo.save(trainingPlan);
        response.setStatus(200); // OK
        return "redirect:/dashboard";
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
    public String deleteTrainingSession(@RequestParam Map<String, String> newSession, @RequestParam("tpid") int tpid,
            @RequestParam("tsid") int tsid,
            Model model) {
        TrainingSession ts = trainingSessionRepo.findBytsid(tsid);

        if (ts == null) {
            model.addAttribute("error", "Training session not found");
            Integer userId = Integer.parseInt(newSession.get("userId"));
            return "redirect:/trainingPlan/viewAll?userId=" + userId;// fix it
        }
        // remove in the training plan
        TrainingPlan tp = trainingPlanRepo.findByTpid(tpid);
        tp.removeTrainingSession(ts);
        trainingPlanRepo.save(tp);
        Integer userId = Integer.parseInt(newSession.get("userId"));
        return "redirect:/trainingPlan/viewAll?userId=" + userId;
    }

}
