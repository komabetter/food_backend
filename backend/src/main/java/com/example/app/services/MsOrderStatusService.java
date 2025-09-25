package com.example.app.services;

import com.example.app.models.MsOrderStatus;
import com.example.app.repositories.MsOrderStatusRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MsOrderStatusService {

    private final MsOrderStatusRepository msOrderStatusRepository;
    private final RedisService redisService;

    public MsOrderStatusService(MsOrderStatusRepository msOrderStatusRepository, RedisService redisService) {
        this.msOrderStatusRepository = msOrderStatusRepository;
        this.redisService = redisService;
    }

    public List<MsOrderStatus> getAllOrderStatuses() {
        // Try to get from Redis cache first
        if (redisService.hasOrderStatusCache()) {
            List<MsOrderStatus> cachedStatuses = redisService.getOrderStatusList();
            if (cachedStatuses != null) {
                System.out.println("Retrieved order statuses from Redis cache");
                return cachedStatuses;
            }
        }

        // If not in cache, get from database
        List<MsOrderStatus> orderStatuses = msOrderStatusRepository.findAll();
        
        // Cache the results
        redisService.setOrderStatusList(orderStatuses);
        System.out.println("Retrieved order statuses from database and cached in Redis");
        
        return orderStatuses;
    }
}