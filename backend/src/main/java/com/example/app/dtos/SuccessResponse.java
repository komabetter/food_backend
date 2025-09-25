package com.example.app.dtos;

import java.time.LocalDateTime;

public class SuccessResponse<T> {
    private String status_code;
    private String status_msg;
    private T data;
    private LocalDateTime timestamp;

    public SuccessResponse() {
    }

    public SuccessResponse(String status_code, T data,String status_msg) {
        this.status_code = status_code;
        this.status_msg = status_msg;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    
}