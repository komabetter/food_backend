package com.example.app.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ms_order_status")
public class MsOrderStatus {
    
    @Id
    private Byte id;
    
    @Column(name = "status_name", nullable = false, unique = true)
    private String statusName;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public MsOrderStatus() {}
    
    public MsOrderStatus(Byte id, String statusName) {
        this.id = id;
        this.statusName = statusName;
    }
    
    // Getters and Setters
    public Byte getId() {
        return id;
    }
    
    public void setId(Byte id) {
        this.id = id;
    }
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}