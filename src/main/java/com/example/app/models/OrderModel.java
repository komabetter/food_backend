package com.example.app.models;

public class OrderModel {

    private String id;
    private int orderStatusId;
    private String orderStatusName;
    private String orderDetail;
    private Float price;
    private String customerName;


    

    public OrderModel(int orderStatusId, String orderStatusName, String orderDetail, Float price, String customerName) {
        this.id = String.valueOf(System.currentTimeMillis());
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

    public void setOrderStatusId(byte orderStatusId) {
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

}
