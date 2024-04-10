package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.service.*;

import jakarta.transaction.Transactional;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.session.SessionAutoConfiguration")
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

    @Test
    public void testRegisterUserExistingUsername() throws Exception {
        String username = "existingUsername";
        String password = "Password123!";

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .param("username", username)
            .param("password", password)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)).andReturn();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
            .param("username", username)
            .param("password", password)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(409, response.getStatus());
    }

    //Login Test
    @Test
    public void testLoginExistingUser() throws Exception {  
        String username = "existingUsername";
        String password = "Password123!";
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
        .param("username", username)
        .param("password", password)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andReturn();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
        .param("username", username)
        .param("password", password)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(302, response.getStatus());
    }

    @Test
    public void testLoginNonExistingUser() throws Exception {  
        String username = "existingUsername";
        String password = "Password123!";
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
        .param("username", username)
        .param("password", password)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andReturn();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
        .param("username", username)
        .param("password", "password")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testClientDashboardNoLogin() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(302, response.getStatus());
    }

    
}
