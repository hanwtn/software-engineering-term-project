package com.example.demo.service;

import com.example.demo.dto.TrainingPlanDTO;
import com.example.demo.dto.TrainingSessionDTO;
import com.example.demo.models.TrainingPlan;
import com.example.demo.models.TrainingSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TrainingPlanService {
    
    public static List<TrainingSessionDTO> toDtoList(List<TrainingSession> sessions) {
        return sessions.stream().map(session -> new TrainingSessionDTO(
                session.getTsid(),
                session.getName(),
                session.getDaysOfWeek(),
                session.getStartTime(),
                session.getEndTime()))
                .collect(Collectors.toList());
    }
    
    
    public List<TrainingPlanDTO> toPlanDtoList(List<TrainingPlan> trainingPlans) {
        List<TrainingPlanDTO> trainingPlanDTOs = new ArrayList<>();
        for (TrainingPlan trainingPlan : trainingPlans) {
            
            LocalDate startDate = trainingPlan.getStartDate();
            LocalDate endDate = trainingPlan.getEndDate();

            
            List<TrainingSessionDTO> sessionDTOs = toDtoList(trainingPlan.getTrainingSessions());; 

            
            TrainingPlanDTO dto = new TrainingPlanDTO(trainingPlan.getTpid(), trainingPlan.getName(), startDate, endDate, sessionDTOs);

            trainingPlanDTOs.add(dto);
        }
        return trainingPlanDTOs;
    }

}
