package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Exercise;
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
public class TrainingPlanController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    @Autowired
    private TrainingSessionRepository trainingSessionRepo;
    private UserService userService = null;

    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/trainingPlan/add/submit")
    public String addPlan(@RequestParam Map<String, String> newPlan,
            @RequestParam(value = "trainingSessions", required = false) String[] selectedSessions, HttpSession session, 
            HttpServletResponse response, Model model) {

        String newName = newPlan.get("name");
        String newDesc = newPlan.get("description");

        int userId = (int) session.getAttribute("userId");

        // Check if name and description are present
        if (newName.isEmpty() || newDesc.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Name or description are not provided");
            model.addAttribute("userId", userId);
            return "training_plans/addTrainingPlan";
        }

        // Check if user exists
        if (!userRepo.existsByUid(userId)) {
            response.setStatus(404); // Not Found
            model.addAttribute("error", "User does not exist");
            model.addAttribute("userId", userId);
            return "training_plans/addTrainingPlan";
        }

        String startDateStr = newPlan.get("sdate");
        String endDateStr = newPlan.get("edate");

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date or end date is not provided");
            model.addAttribute("userId", userId);
            return "training_plans/addTrainingPlan";
        }
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // check if start date is before end date
        if (startDate.isAfter(endDate)) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date is after end date");
            model.addAttribute("userId", userId);
            return "training_plans/addTrainingPlan";
        }
        // check if start date and end date are valid
        //TO DO: I think we should allow users to add start date and end dates before the current date, give them more freedom
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start or end date is before today");
            return "users/loginPage";
        }
        TrainingPlan newTrainingPlan = new TrainingPlan(newName, newDesc, startDate, endDate);
        User user = userRepo.findByUid(userId);
        System.out.println("Training Session Names:");

        /*
         * for (int i = 0; i < ts.size(); i++) {
         * System.out.println(ts.get(i).getName());
         * }
         * System.out.println("NEW TRAINING PLAN TEST:");
         * for (int i = 0; i < newTrainingPlan.getTrainingSessions().size(); i++) {
         * System.out.println(newTrainingPlan.getTrainingSessions().get(i).getName());
         * }
         */

        // add error-check for getting user
        user.addTrainingPlan(newTrainingPlan);
        userRepo.save(user); // Save the user with the new training plan
        // removed user from training plan constructor
        System.out.println("Successfully Added");
        return "redirect:/dashboard";
    }

    @GetMapping("/trainingPlan/add")
    public String trainingPlanTest(HttpSession session, HttpServletResponse response, Model model) {
        try {
            int userId = (int) session.getAttribute("userId");
            User user = userRepo.findByUid(userId);
            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("userId", userId);
                return "training_plans/addTrainingPlan";
            } else {
                // Handling case where user does not exist
                response.setStatus(404); // Not Found
                model.addAttribute("error", "User does not exist");
                return "training_plans/addTrainingPlan";
            }
        } catch (NumberFormatException e) {
            // Handling case where userId is not a valid integer
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Invalid User ID");
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/trainingPlan/viewAll")
    public String viewTrainingPlan(@RequestParam Map<String, String> newUser, HttpServletResponse response,
            Model model) {
        Integer userId = Integer.parseInt(newUser.get("userId"));
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<TrainingPlan> trainingPlans = user.getTrainingPlans();
        for (TrainingPlan plan : trainingPlans) {
            System.out.println("Plan: " + plan.getName());
            for (TrainingSession session : plan.getTrainingSessions()) {
                System.out.println("  Session: " + session.getName());
                for (Exercise exercise : session.getExercises()) {
                    System.out.println("    Exercise: " + exercise.getName());
                }
            }
        }
        for (TrainingPlan plan : trainingPlans) {
            System.out.println("Plan: " + plan.getName());
            for (TrainingSession session : plan.getTrainingSessions()) {
                System.out.println("  Session: " + session.getName());
                // Log the days of the week for each session
                System.out.println("    Days of Week: " + session.getDaysOfWeek());
                for (Exercise exercise : session.getExercises()) {
                    System.out.println("    Exercise: " + exercise.getName());
                }
            }
        }
        model.addAttribute("trainingPlans", trainingPlans);


        if (!trainingPlans.isEmpty()) {
            // Accessing the first training plan if the list is not empty
            System.out.println(trainingPlans.get(0).getName() + " plan :");
            System.out.println(trainingPlans.get(0).getTrainingSessions());
        }
        return "training_plans/viewAllTrainingPlan";
    }

    @PostMapping("/trainingPlan/delete")
    public String deleteTrainingPlan(@RequestParam Map<String, String> deleteForm, Model model) {
        int userId = Integer.parseInt(deleteForm.get("userId"));
        int tpid = Integer.parseInt(deleteForm.get("tpid"));

        User user = userRepo.findByUid(userId);
        if (user != null) {
            // Find the training plan by ID
            TrainingPlan planToDelete = user.getTrainingPlans().stream()
                    .filter(plan -> plan.getTpid() == tpid)
                    .findFirst()
                    .orElse(null);

            if (planToDelete != null) {
                user.removeTrainingPlan(planToDelete);
                userRepo.save(user); // Save the user to update the training plan list
                // unnessesary apparently
                // trainingPlanRepo.delete(planToDelete);
                model.addAttribute("success", "Training plan deleted successfully");
            } else {
                model.addAttribute("error", "Training plan not found");
            }
        } else {
            model.addAttribute("error", "User not found");
        }
        return "redirect:/trainingPlan/viewAll?userId=" + userId;
    }

    @GetMapping("/trainingPlan/edit/{tpid}")
    public String editTrainingPlanForm(@PathVariable("tpid") int tpid, Model model) {
        TrainingPlan trainingPlan = trainingPlanRepo.findById(tpid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid training plan ID: " + tpid));
        model.addAttribute("trainingPlan", trainingPlan);
        return "training_plans/editTrainingPlan";
    }

    @PostMapping("/trainingPlan/edit/{tpid}")
    public String updateTrainingPlan(@PathVariable("tpid") int tpid,
            @RequestParam Map<String, String> updatedPlan, Model model) {
        TrainingPlan trainingPlan = trainingPlanRepo.findById(tpid)
            .orElseThrow(() -> new IllegalArgumentException("Invalid training plan ID: " + tpid));

        String newName = updatedPlan.get("name");
        String newDesc = updatedPlan.get("description");
        String newStartDateStr = updatedPlan.get("startDate");
        String newEndDateStr = updatedPlan.get("endDate");

        // Validate input data
        if (newName == null || newName.isEmpty() || newDesc == null || newDesc.isEmpty() ||
                newStartDateStr == null || newStartDateStr.isEmpty() || newEndDateStr == null || newEndDateStr.isEmpty()) {
            model.addAttribute("error", "All fields are required.");
            model.addAttribute("trainingPlan", trainingPlan);
            return "training_plans/editTrainingPlan";
        }

        LocalDate newStartDate = LocalDate.parse(newStartDateStr);
        LocalDate newEndDate = LocalDate.parse(newEndDateStr);

        if (newStartDate.isAfter(newEndDate)) {
            model.addAttribute("error", "Start date must be before end date.");
            model.addAttribute("trainingPlan", trainingPlan);
            return "training_plans/editTrainingPlan";
        }

        // Update training plan
        trainingPlan.setName(newName);
        trainingPlan.setDescription(newDesc);
        trainingPlan.setStartDate(newStartDate);
        trainingPlan.setEndDate(newEndDate);

        trainingPlanRepo.save(trainingPlan);
        return "redirect:/trainingPlan/viewAll?userId=" + trainingPlan.getUser().getUid();
    }



}
