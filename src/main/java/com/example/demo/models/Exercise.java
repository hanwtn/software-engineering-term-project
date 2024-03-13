package com.example.demo.models;
import jakarta.persistence.*;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eid;

    private String name;
    private String description;
    private String intensity;
    private int sets;
    private int reps;
    private int duration; //currently not used, do we want to comment it out and leave it for next iteration? 
    
    public Exercise() {
    }

    public Exercise(String name, String description, int sets, int reps, String intensity) {
        this.name = name;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
        this.intensity = intensity;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }
}
