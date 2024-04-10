package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.models.TrainingPlan;
import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void viewTrainingPlanNotLoggedIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trainingPlan/viewAll"))
                .andReturn();
        assertEquals(302, result.getResponse().getStatus());
    }

    @Test
    public void viewTrainingPlanLoggedIn() throws Exception {
        User user = new User();
        user.setUid(1);
        user.setUsername("testUser");
        user.setTrainingPlans(new ArrayList<>());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        when(userRepository.findByUid(1)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trainingPlan/viewAll")
                .session(session))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void addTrainingPlanNotLoggedIn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trainingPlan/add"))
                .andReturn();
        assertEquals(302, result.getResponse().getStatus());
    }

    @Test
    public void addTrainingPlanLoggedIn() throws Exception {
        User user = new User();
        user.setUid(1);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        when(userRepository.findByUid(1)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/trainingPlan/add")
                .session(session))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    // @Test
    // public void submitTrainingPlan() throws Exception {
    //     // Arrange
    //     User user = new User();
    //     user.setUid(1); // make sure this user has an ID set
    //     user.setTrainingPlans(new ArrayList<>()); // Set up training plans or other necessary fields

    //     MockHttpSession session = new MockHttpSession();
    //     session.setAttribute("userId", user.getUid()); // Set the user ID in the session

    //     when(userRepository.findByUid(user.getUid())).thenReturn(user); // Mock the user repository

    //     
    //     mockMvc.perform(MockMvcRequestBuilders.post("/trainingPlan/add/submit")
    //             .param("name", "Plan 1")
    //             .param("description", "This is a test plan")
    //             .param("sdate", LocalDate.now().toString())
    //             .param("edate", LocalDate.now().plusWeeks(4).toString())
    //             .session(session)
    //             .contentType(MediaType.APPLICATION_FORM_URLENCODED))
    //             .andExpect(status().isFound()) // Expect a 302 redirect status
    //             .andReturn();

    //     verify(userRepository, times(1)).save(any(User.class)); // Verify the user repository save method was called
    // }


    @Test
    public void deleteTrainingPlan() throws Exception {
        User user = new User();
        user.setUid(1);
        TrainingPlan plan = new TrainingPlan();
        plan.setTpid(1);
        List<TrainingPlan> plans = new ArrayList<>();
        plans.add(plan);
        user.setTrainingPlans(plans);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        when(userRepository.findByUid(1)).thenReturn(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/trainingPlan/delete")
                .param("tpid", "1")
                .session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        assertEquals(302, result.getResponse().getStatus());
        verify(userRepository, times(1)).save(any(User.class));
    }
}
