package com.example.app.services;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.app.models.MsOrderStatus;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ORDER_STATUS_LIST_KEY = "order_status_list";
    private static final long CACHE_TTL = 300; // 5 minutes

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

   
    // ===== MsOrderStatus Redis Methods =====
    // Check if order status exists in cache
    public boolean hasOrderStatusCache() {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(ORDER_STATUS_LIST_KEY));
        } catch (Exception e) {
            System.err.println("Error checking order status cache existence: " + e.getMessage());
            return false;
        }
    }

    // Set individual order status in Redis
    public void setOrderStatusList(List<MsOrderStatus> orderStatus) {
        try {
            String key = ORDER_STATUS_LIST_KEY;
            redisTemplate.opsForValue().set(key, orderStatus, CACHE_TTL, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Error caching order status: " + e.getMessage());
        }
    }

    // Get order status from Redis
    @SuppressWarnings("unchecked")
    public List<MsOrderStatus> getOrderStatusList() {
        try {
            String key = ORDER_STATUS_LIST_KEY;
            Object cachedOrderStatus = redisTemplate.opsForValue().get(key);
            if (cachedOrderStatus != null) {
                return (List<MsOrderStatus>) cachedOrderStatus;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving order status from cache: " + e.getMessage());
        }
        return null;
    }

}
       