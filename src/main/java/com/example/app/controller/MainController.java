package com.example.app.controller;

import com.example.app.services.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MainController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;

    public MainController(RedisTemplate<String, Object> redisTemplate, RedisService redisService) {
        this.redisTemplate = redisTemplate;
        this.redisService = redisService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to Spring Boot API - Hello World!";
    }

    @GetMapping("/redis-check")
    public Map<String, Object> checkRedisConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Test basic Redis connectivity by setting and getting a test value
            String testKey = "connection_test";
            String testValue = "success";
            
            redisTemplate.opsForValue().set(testKey, testValue);
            String retrievedValue = (String) redisTemplate.opsForValue().get(testKey);
            
            // Clean up test key
            // redisTemplate.delete(testKey);
            
            if (testValue.equals(retrievedValue)) {
                response.put("status", "success");
                response.put("message", "Redis connection is working properly");
                response.put("testKey", testKey);
                response.put("testValue", retrievedValue);
            } else {
                response.put("status", "error");
                response.put("message", "Redis connection test failed - values do not match");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Redis connection failed: " + e.getMessage());
        }
        return response;
    }
}