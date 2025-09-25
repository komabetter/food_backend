package com.example.app.controller;

import com.example.app.models.MsOrderStatus;
import com.example.app.services.MsOrderStatusService;
import com.example.app.services.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-statuses")
public class MsOrderStatusController {

    private MsOrderStatusService msOrderStatusService;
    private RedisService redisService;

    public MsOrderStatusController(MsOrderStatusService msOrderStatusService, RedisService redisService) {
        this.msOrderStatusService = msOrderStatusService;
        this.redisService = redisService;
    }

    @GetMapping
    public ResponseEntity<List<MsOrderStatus>> getAllOrderStatuses() {

        if (redisService.hasOrderStatusCache()) {
            List<MsOrderStatus> cachedStatuses = redisService.getOrderStatusList();
            if (cachedStatuses != null) {
                System.out.println("Retrieved order statuses from Redis cache");
                return new ResponseEntity<>(cachedStatuses, HttpStatus.OK);
            }
        }

        List<MsOrderStatus> orderStatuses = msOrderStatusService.getAllOrderStatuses();
        // redisService.setOrderStatusList(orderStatuses);
        return new ResponseEntity<>(orderStatuses, HttpStatus.OK);
    }

}