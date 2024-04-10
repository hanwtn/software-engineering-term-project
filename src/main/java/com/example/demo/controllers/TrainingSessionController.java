package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/trainingSession/add")
    public String trainingSessionAdd(@RequestParam("tpid") int tpid, HttpSession session,
            HttpServletResponse response,
            Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
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
            @RequestParam(value = "daysOfWeek", required = false) String[] daysOfWeekArray,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Time startTime = Time.valueOf(startTimeString);
        Time endTime = Time.valueOf(endTimeString);
        String name = newSession.get("name");
        List<Exercise> exercises = new ArrayList<>();
        Set<DayOfWeek> daysOfWeek = Arrays.stream(daysOfWeekArray).map(String::toUpperCase).map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());

        // if (daysOfWeekArray != null) {
        // daysOfWeek =
        // Arrays.stream(daysOfWeekArray).map(String::toUpperCase).map(DayOfWeek::valueOf)
        // .collect(Collectors.toSet());
        // } else {
        // daysOfWeek = null;
        // }

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

        TrainingPlan trainingPlan = trainingPlanRepo.findByTpid(tpid);

        TrainingSession newTrainingSession = new TrainingSession(exercises, daysOfWeek, Time.valueOf(startTimeString),
                Time.valueOf(endTimeString), newSession.get("name"));

        trainingPlan.addTrainingSession(newTrainingSession);
        if (selectedExercises != null) {
            for (String exerciseId : selectedExercises) {
                Exercise exercise = exerciseRepository.findById(Integer.parseInt(exerciseId))
                        .orElseThrow(() -> new IllegalArgumentException("Invalid exercise ID: " + exerciseId));
                newTrainingSession.addExercise(exercise);
                exercise.addTrainingSession(newTrainingSession);
            }
        }
        trainingPlanRepo.save(trainingPlan);
        response.setStatus(200); // OK
        return "redirect:/trainingPlan/viewAll";
    }

    @GetMapping("/trainingSession/add/search")
    public String trainingSessionSearch(@RequestParam Map<String, String> newUser, HttpServletResponse response,
            Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        String search = newUser.get("search");
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<Exercise> allExercises = exerciseRepository.findByNameIgnoreCaseContaining(search);
        model.addAttribute("exercises", allExercises);
        return "training_sessions/addTrainingSession";
    }

    @GetMapping("/trainingSession/view")
    public String viewTrainingSessionView(@RequestParam Map<String, String> newSession, HttpServletResponse response,
            Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        List<TrainingSession> trainingSessions = trainingSessionRepo.findAll();
        model.addAttribute("trainingSessions", trainingSessions);
        System.out.println("HERE");
        System.out.println(trainingSessions.get(0).getName());
        return "training_sessions/viewTrainingSession";
    }

    @PostMapping("/trainingSession/delete")
    public String deleteTrainingSession(@RequestParam Map<String, String> newSession, @RequestParam("tsid") int tsid,
            Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        TrainingSession ts = trainingSessionRepo.findBytsid(tsid);

        if (ts == null) {
            model.addAttribute("error", "Training session not found");
            return "redirect:/trainingPlan/viewAll";
        }

        ts.getExercises().forEach(exercise -> {
            exercise.getTrainingSessions().remove(ts);
            exerciseRepository.save(exercise);
        });

        trainingSessionRepo.delete(ts);
        return "redirect:/trainingPlan/viewAll";
    }

    @GetMapping("/trainingSession/edit")
    public String editTrainingSessionForm(@RequestParam("tsid") int tsid, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        TrainingSession trainingSession = trainingSessionRepo.findById(tsid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid training session ID: " + tsid));

        model.addAttribute("trainingSession", trainingSession);
        model.addAttribute("allExercises", exerciseRepository.findAll());
        return "training_sessions/editTrainingSession";
    }

    @PostMapping("/trainingSession/edit")
    public String updateTrainingSession(@RequestParam("tsid") int tsid,
            @RequestParam Map<String, String> updatedSession,
            @RequestParam(value = "exercises", required = false) List<Integer> selectedExerciseIds,
            @RequestParam(value = "daysOfWeek[]", required = false) Set<DayOfWeek> daysOfWeek,
            Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        TrainingSession trainingSession = trainingSessionRepo.findById(tsid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid training session ID: " + tsid));
        String name = updatedSession.get("name");
        Time startTime = Time.valueOf(updatedSession.get("startTime"));
        Time endTime = Time.valueOf(updatedSession.get("endTime"));

        Validation validate = TrainingSessionService.validate(name, daysOfWeek, startTime, endTime);
        if (validate.isError) {
            model.addAttribute("error", validate.message);
            model.addAttribute("trainingSession", trainingSession);
            model.addAttribute("allExercises", exerciseRepository.findAll());
            return "training_sessions/editTrainingSession";
        }

        trainingSession.setName(name);
        trainingSession.setStartTime(startTime);
        trainingSession.setEndTime(endTime);
        trainingSession.setDaysOfWeek(daysOfWeek);

        trainingSession.getExercises().clear();
        if (selectedExerciseIds != null) {
            List<Exercise> selectedExercises = exerciseRepository.findAllById(selectedExerciseIds);
            trainingSession.setExercises(selectedExercises);
        }

        trainingSessionRepo.save(trainingSession);

        return "redirect:/trainingPlan/viewAll";
    }

}
