package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    //TEST THE REGISTER
    @Test
    public void testRegisterUserInvalid() throws Exception {
        String username = "123";
        String password = "bad";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testRegisterUserValid() throws Exception {
        String username = "Username123!";
        String password = "Password123!";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(201, response.getStatus());
    }

    
}
