package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/trainingPlan/viewAll")
    public String viewTrainingPlan(@RequestParam Map<String, String> newUser, HttpServletResponse response,
            Model model, HttpSession session) {
        // check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        Integer userId = (int) session.getAttribute("userId");
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        List<TrainingPlan> trainingPlans = user.getTrainingPlans();
        model.addAttribute("trainingPlans", trainingPlans);

        if (!trainingPlans.isEmpty()) {
            // Accessing the first training plan if the list is not empty
            System.out.println(trainingPlans.get(0).getName() + " plan :");
            System.out.println(trainingPlans.get(0).getTrainingSessions());
        }
        return "training_plans/viewAllTrainingPlan";
    }

    @GetMapping("/trainingPlan/add")
    public String trainingPlanTest(HttpSession session, HttpServletResponse response, Model model) {
        try {
            // check if user is logged in
            if (session.getAttribute("userId") == null) {
                return "redirect:/login";
            }
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
            return "users/addTrainingPlan";
        }
    }

    @PostMapping("/trainingPlan/add/submit")
    public String addPlan(@RequestParam Map<String, String> newPlan,
            @RequestParam(value = "trainingSessions", required = false) String[] selectedSessions, HttpSession session,
            HttpServletResponse response, Model model) {

        // check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        int userId = (int) session.getAttribute("userId");
        String newName = newPlan.get("name");
        String newDesc = newPlan.get("description");

        // Check if name and description are present
        if (newName.isEmpty() || newDesc.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Name or description are not provided");
            model.addAttribute("userId", userId);
            return "redirect:/trainingPlan/add";
        }

        // Check if user exists
        if (!userRepo.existsByUid(userId)) {
            response.setStatus(404); // Not Found
            model.addAttribute("error", "User does not exist");
            model.addAttribute("userId", userId);
            return "redirect:/trainingPlan/add";
        }

        String startDateStr = newPlan.get("sdate");
        String endDateStr = newPlan.get("edate");

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date or end date is not provided");
            model.addAttribute("userId", userId);
            return "users/addTrainingPlan";
        }
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // check if start date is before end date
        if (startDate.isAfter(endDate)) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date is after end date");
            model.addAttribute("userId", userId);
            return "users/addTrainingPlan";
        }
        // check if start date and end date are valid
        // TO DO: I think we should allow users to add start date and end dates before
        // the current date, give them more freedom
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
        if (user != null) {
            user.addTrainingPlan(newTrainingPlan);
            userRepo.save(user); // Save the user with the new training plan
            System.out.println("Successfully Added");
        } else {
            System.out.println("Attempted to add training plan to a null User.");
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/trainingPlan/delete")
    public String deleteTrainingPlan(@RequestParam Map<String, String> deleteForm, Model model, HttpSession session) {
        // check if user is logged in
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        int userId = (int) session.getAttribute("userId");
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
        return "redirect:/trainingPlan/viewAll";
    }

}
