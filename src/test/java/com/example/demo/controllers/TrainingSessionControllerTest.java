package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.models.ExerciseRepository;
import com.example.demo.models.TrainingPlanRepository;
import com.example.demo.models.TrainingSessionRepository;
import com.example.demo.models.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TrainingSessionController.class)
public class TrainingSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private TrainingPlanRepository trainingPlanRepo;

    @MockBean
    private TrainingSessionRepository trainingSessionRepo;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @Test
    public void simpleTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    // Other test methods...
}
