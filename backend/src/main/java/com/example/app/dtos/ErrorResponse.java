package com.example.app.dtos;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String status_code;
    private String status_msg;
    private LocalDateTime timestamp;

    public ErrorResponse() {}

    public ErrorResponse(String status_code, String status_msg) {
        this.status_code = status_code;
        this.status_msg = status_msg;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    
}