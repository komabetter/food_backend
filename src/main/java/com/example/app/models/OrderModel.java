package com.example.app.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "food_orders")
public class OrderModel {

    @Id
    private String id;
    
    @Column(name = "order_status_id")
    private int orderStatusId;
    
    @Column(name = "order_status_name")
    private String orderStatusName;
    
    @Column(name = "order_detail")
    private String orderDetail;
    
    @Column(name = "price")
    private Float price;
    
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public OrderModel() {
        this.id = UUID.randomUUID().toString();
    }

    public OrderModel(int orderStatusId, String orderStatusName, String orderDetail, Float price, String customerName) {
        this.id = UUID.randomUUID().toString();
        this.orderStatusId = orderStatusId;
        this.orderStatusName = orderStatusName;
        this.orderDetail = orderDetail;
        this.price = price;
        this.customerName = customerName;
    }

    public OrderModel(String id, int orderStatusId, String orderStatusName, String orderDetail, Float price,
            String customerName) {
        this.id = id;
        this.orderStatusId = orderStatusId;
        this.orderStatusName = orderStatusName;
        this.orderDetail = orderDetail;
        this.price = price;
        this.customerName = customerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
