package com.example.demo.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.TrainingPlan;
import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;
// import com.example.quizapp2.models.Users;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
    private final int USERNAME_MIN_LENGTH = 4;
    private final int PASSWORD_MIN_LENGTH = 8;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TrainingPlanRepository trainingPlanRepo;
    private UserService userService = null;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public void UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseBody
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    // simple get request to login page
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if(session.getAttribute("userId") != null){
            return "redirect:/dashboard";
        }
        
        return "users/loginPage";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "users/registerPage";
    }

    @GetMapping("/users/logout")
    public String logout(HttpSession session) {
        System.out.println("Logging out");
        session.invalidate(); // Invalidate the session to logout the user
        return "users/loginPage";
    }

    @PostMapping("/users/login")
    public String login(@RequestParam Map<String, String> newUser,HttpSession session, HttpServletResponse response, Model model) {
        System.out.println("Logging in");

        String newUsername = newUser.get("username");
        String newPassword = newUser.get("password");

        // Check if username and password are present
        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username or password not provided");
            return "users/loginPage";
        }

        // Check if username reaches minimum length
        if (newUsername.length() < USERNAME_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
            return "users/loginPage";
        }

        // Check if password reaches minimum length
        if (newPassword.length() < PASSWORD_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Password must be at least " + PASSWORD_MIN_LENGTH + " characters");
            return "users/loginPage";
        }

        User user = userRepo.findByUsername(newUsername);
        if (user == null) {
            response.setStatus(404); // Not Found
            model.addAttribute("error", "Invalid username");
            return "users/loginPage";
        }
        if (!user.getPassword().equals(newPassword)) {
            response.setStatus(401); // Unauthorized
            model.addAttribute("error", "Invalid password");
            return "users/loginPage";
        }

        // login successful
        session.setAttribute("userId", user.getUid());
        // if 0 go to user page
        if (user.getStatus() == 0) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            return "redirect:/dashboard";
        }

        // TODO: (low priority) Reconsider/remove Coach as it is no longer used
        // if 1 go to coach page
        if (user.getStatus() == 1) {
            response.setStatus(200); // OK
            model.addAttribute("user", user);
            model.addAttribute("trainingPlans", trainingPlanRepo.getAllTrainingPlansByUser(user));
            return "redirect:/dashboard"; //coach view is same as user for now, more to come
        }

        // default to login page
        response.setStatus(401); // Unauthorized
        model.addAttribute("error", "Invalid status");
        return "users/loginPage";
    }
    private boolean isPasswordValid(String password){
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        
        boolean hasSymbol = password.matches(".*\\W.*");
        boolean isLongEnough = password.length()>= PASSWORD_MIN_LENGTH;
        
        return hasUppercase && hasLowercase && hasSymbol && isLongEnough;
    }

    @PostMapping("/users/register")
    public String registerUser(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        System.out.println("ADD user");

        String newUsername = newUser.get("username");
        int newStatus = Integer.parseInt(newUser.getOrDefault("status", "0"));
        String newPassword = newUser.get("password");

        // Check if username and password are present
        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username or password not provided");
            return "users/registerPage";
        }

        // Check if username reaches minimum length
        if (newUsername.length() < USERNAME_MIN_LENGTH) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Username must be at least " + USERNAME_MIN_LENGTH + " characters");
            return "users/registerPage";
        }

        if (!isPasswordValid(newPassword)) {
            response.setStatus(400);
            model.addAttribute("error", "Password must be at least " + PASSWORD_MIN_LENGTH + 
                               " characters and include at least one uppercase letter, one lowercase letter, and one symbol");
            return "users/registerPage";
        }

        // username already exists
        if (userRepo.existsByUsername(newUsername)) {
            System.out.println("Username already exists");
            response.setStatus(409); // Conflict
            model.addAttribute("error", "Username already exists");
            return "users/registerPage";
        }

        userRepo.save(new User(newUsername, newPassword, newStatus));
        response.setStatus(201);
        model.addAttribute("success", "User created");
        return "users/loginPage";
    }

    @PostMapping("/users/deleteAll")
    public String deleteAllUsers(HttpServletResponse response) {
        System.out.println("DELETE all users");
        userRepo.deleteAll();
        response.setStatus(204);
        return "redirect:/users/view";
    }

    //TEMP
    /* 
    @GetMapping("/trainingPlan")
    public String trainingPlanTest(@RequestParam Map<String, String> newUser, HttpServletResponse response, Model model) {
        User user = userRepo.findByUsername("Trainer");
        model.addAttribute("user", user);
        TrainingPlan trainingPlan = new TrainingPlan("Training Plan 1", "Description", user);
        model.addAttribute("trainingPlan", trainingPlan);
        return "users/trainingPlanPage";
    
    }
    */
    @PostMapping("/trainingPlan/add")
    public String addPlan(@RequestParam Map<String, String> newPlan, HttpServletResponse response, Model model){

        String newName = newPlan.get("name");
        String newDesc = newPlan.get("description");

        // Check if name and description are present
        if (newName.isEmpty() || newDesc.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Name or description are not provided");
            return "users/loginPage";
        }

        int userId = Integer.parseInt(newPlan.get("userId"));
       
        // Check if user exists
        if (!userRepo.existsByUid(userId)) {
            response.setStatus(404); // Not Found
            model.addAttribute("error", "User does not exist");
            return "users/loginPage";
        }

        String startDateStr = newPlan.get("sdate");
        String endDateStr = newPlan.get("edate");

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date or end date are not provided");
            return "users/loginPage";
        }
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        //check if start date is before end date
        if (startDate.isAfter(endDate)) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start date is after end date");
            return "users/loginPage";
        }
        //check if start date and end date are valid
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            response.setStatus(400); // Bad Request
            model.addAttribute("error", "Start or end date is before today");
            return "users/loginPage";
        }

        trainingPlanRepo.save(new TrainingPlan(newName, newDesc, userRepo.findByUid(userId), startDate, endDate));
        System.out.println("Successfully Added");
        return "redirect:/dashboard";
    }

    @GetMapping("/trainingPlan")
    public String trainingPlanTest(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId == null) {
            return "redirect:/login";
        }
        Optional<User> userOptional = userRepo.findById(userId);
        if(!userOptional.isPresent()) {
            // Handle the case where the user is not found
            return "redirect:/login";
        }
        User user = userOptional.get();
        model.addAttribute("user", user);
        return "users/addTrainingPlan";
    }
    
    /* 
    @Transactional
    @PostMapping("/trainingPlan/delete")
    public String deleteTrainingPlan(@RequestParam Map<String, String> deleteForm, Model model) {
        // Implement logic to delete the training plan by ID
        int userId = Integer.parseInt(deleteForm.get("userId"));
        int tpid = Integer.parseInt(deleteForm.get("tpid"));
        trainingPlanRepo.deleteBytpid(tpid);
    
        User user = userRepo.findByUid(userId);
        model.addAttribute("user", user);
        return "redirect:/trainingPlan";
    }
    */
    

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        Integer uId = (Integer) session.getAttribute("userId");
        if (uId == null) {
            return "redirect:/login"; 
        }

        Optional<User> uOptional = userRepo.findById(uId);
        if (!uOptional.isPresent()) {
            session.invalidate();
            return "redirect:/login";
        }
        User loggedInUser = uOptional.get();
        model.addAttribute("user", loggedInUser);
        
        
        List<TrainingPlan> trainingPlans = trainingPlanRepo.getAllTrainingPlansByUser(loggedInUser);

        
        String trainingPlansJson = convertToJson(trainingPlans);
        model.addAttribute("trainingPlansJson", trainingPlansJson);
            return "users/dashboard"; 
    }

    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]"; 
        }
    }

    
    @GetMapping("/accEdit/{userId}")
    public String editAccountForm(@PathVariable("userId") Integer uId, HttpSession session, Model model, HttpServletRequest request) {
        Integer sessionUid = (Integer) session.getAttribute("userId");
        if (sessionUid == null || !sessionUid.equals(uId)) {
            return "redirect:/login";
        }

        Optional<User> uOptional = userRepo.findById(uId);
        if (!uOptional.isPresent()) {
            return "redirect:/login"; 
        }

        model.addAttribute("user", uOptional.get());
        return "users/accEdit"; 
    }

    @PostMapping("/accEdit/{userId}")
    public String updateAccount(@PathVariable("userId") Integer uId, @RequestParam Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes) {
        Integer sessionUid = (Integer) session.getAttribute("userId");
        if (sessionUid == null || !sessionUid.equals(uId)) {
            return "redirect:/login";
        }

        User user = userRepo.findById(uId).orElse(null);
        if (user == null) {
            return "redirect:/login"; 
        }
        String newUname = params.get("username");
        if (!user.getUsername().equals(newUname) && userRepo.existsByUsername(newUname)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists. Please choose a different username.");
            return "redirect:/accEdit/" + uId; 
        }

        user.setUsername(params.get("username"));
        user.setWeight(Double.valueOf(params.get("weight")));
        user.setHeight(Double.valueOf(params.get("height")));
        userService.saveUser(user);
        return "redirect:/dashboard"; 
    }  


    @GetMapping("/accDetails/{userId}")
    public String accountDetails(@PathVariable("userId") Integer Uid, HttpSession session, Model model) {
        Integer sessionUid = (Integer) session.getAttribute("userId");
        if(sessionUid == null || !sessionUid.equals(Uid)) {
            return "redirect:/login";
        }

        Optional<User> userOptional = userRepo.findById(Uid);
        if(!userOptional.isPresent()) {
            return "redirect:/login";
        }

        model.addAttribute("user", userOptional.get());
        return "users/accDetails";
    }


}


    
