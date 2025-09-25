package com.example.app.controller;

import com.example.app.models.MsOrderStatus;
import com.example.app.services.MsOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-statuses")
public class MsOrderStatusController {
    
    @Autowired
    private MsOrderStatusService msOrderStatusService;
    
    @GetMapping
    public ResponseEntity<List<MsOrderStatus>> getAllOrderStatuses() {
        List<MsOrderStatus> orderStatuses = msOrderStatusService.getAllOrderStatuses();
        return new ResponseEntity<>(orderStatuses, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MsOrderStatus> getOrderStatusById(@PathVariable Byte id) {
        Optional<MsOrderStatus> orderStatus = msOrderStatusService.getOrderStatusById(id);
        return orderStatus.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping
    public ResponseEntity<MsOrderStatus> createOrderStatus(@RequestBody MsOrderStatus orderStatus) {
        if (msOrderStatusService.existsByStatusName(orderStatus.getStatusName())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        MsOrderStatus createdOrderStatus = msOrderStatusService.createOrderStatus(orderStatus);
        return new ResponseEntity<>(createdOrderStatus, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MsOrderStatus> updateOrderStatus(@PathVariable Byte id, @RequestBody MsOrderStatus orderStatusDetails) {
        Optional<MsOrderStatus> updatedOrderStatus = msOrderStatusService.updateOrderStatus(id, orderStatusDetails);
        return updatedOrderStatus.map(orderStatus -> new ResponseEntity<>(orderStatus, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Byte id) {
        if (msOrderStatusService.deleteOrderStatus(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}