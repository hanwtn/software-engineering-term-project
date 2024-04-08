package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

public class TrainingPlanDTO {
    private int tpid;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<TrainingSessionDTO> sessions;

    public TrainingPlanDTO() {}

    public TrainingPlanDTO(int tpid, String name, LocalDate startDate, LocalDate endDate, List<TrainingSessionDTO> sessions) {
        this.tpid = tpid;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sessions = sessions;
    }

    public int getTpid() {
        return tpid;
    }

    public void setTpid(int tpid) {
        this.tpid = tpid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<TrainingSessionDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<TrainingSessionDTO> sessions) {
        this.sessions = sessions;
    }
}
