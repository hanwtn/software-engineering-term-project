package com.example.demo.dto;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Set;

public class TrainingSessionDTO {
    private int tsid;
    private String name;
    private Set<DayOfWeek> daysOfWeek;
    private Time startTime;
    private Time endTime;

    public TrainingSessionDTO() {}

    public TrainingSessionDTO(int tsid, String name, Set<DayOfWeek> daysOfWeek, Time startTime, Time endTime) {
        this.tsid = tsid;
        this.name = name;
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getTsid() {
        return tsid;
    }

    public void setTsid(int tsid) {
        this.tsid = tsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
