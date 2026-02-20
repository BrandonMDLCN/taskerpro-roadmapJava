package com.tasktraker.taskerpro.clases;

import java.time.LocalDate;

public class Task {
    private String id;
    private String description;
    private String status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Task(String id, String description, String status, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString(){
        return "{id= "+this.id+", description= "+this.description+", status= "+this.status+", Created= "+this.createdAt+", LastUpdated= "+this.updatedAt+"}";
    }
}