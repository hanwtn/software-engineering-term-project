package com.example.demo.models;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TrainingSessionDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private TrainingSession session;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek;

    private LocalDate startDate;
    private LocalDate endDate;

    public TrainingSessionDate() {
        this.session = new TrainingSession();
        this.daysOfWeek = new HashSet<>();
    }

    public TrainingSessionDate(TrainingSession session, Set<DayOfWeek> daysOfWeek, LocalDate startDate, LocalDate endDate) {
        this.session = new TrainingSession();
        this.session.setExercises(new ArrayList<>(session.getExercises()));
        this.daysOfWeek = daysOfWeek;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TrainingSession getSession() {
        return session;
    }

    public void setSession(TrainingSession session) {
        this.session = session;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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
}
