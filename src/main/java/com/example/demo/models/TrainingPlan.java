package com.example.demo.models;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TrainingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tpid;

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    private List<TrainingSessionDate> sessions;

    public TrainingPlan() {
        this.sessions = new ArrayList<>();
    }

    public void addSession(TrainingSessionDate sessionDate) {
        this.sessions.add(sessionDate);
    }

    public List<TrainingSessionDate> getSessions() {
        return sessions;
    }

    public void setSessions(List<TrainingSessionDate> sessions) {
        this.sessions = sessions;
    }

    public int getId() {
        return tpid;
    }

    public void setId(int tpid) {
        this.tpid = tpid;
    }
    
}
