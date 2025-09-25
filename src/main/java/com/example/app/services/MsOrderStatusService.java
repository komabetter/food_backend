
package com.example.app.services;

import com.example.app.models.MsOrderStatus;
import com.example.app.repositories.MsOrderStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MsOrderStatusService {
    
    @Autowired
    private MsOrderStatusRepository msOrderStatusRepository;
    
    public List<MsOrderStatus> getAllOrderStatuses() {
        return msOrderStatusRepository.findAll();
    }
    
    public Optional<MsOrderStatus> getOrderStatusById(Byte id) {
        return msOrderStatusRepository.findById(id);
    }
    
    public MsOrderStatus createOrderStatus(MsOrderStatus orderStatus) {
        orderStatus.setCreatedAt(LocalDateTime.now());
        orderStatus.setUpdatedAt(LocalDateTime.now());
        return msOrderStatusRepository.save(orderStatus);
    }
    
    public Optional<MsOrderStatus> updateOrderStatus(Byte id, MsOrderStatus orderStatusDetails) {
        return msOrderStatusRepository.findById(id).map(orderStatus -> {
            orderStatus.setStatusName(orderStatusDetails.getStatusName());
            orderStatus.setUpdatedAt(LocalDateTime.now());
            return msOrderStatusRepository.save(orderStatus);
        });
    }
    
    public boolean deleteOrderStatus(Byte id) {
        return msOrderStatusRepository.findById(id).map(orderStatus -> {
            msOrderStatusRepository.delete(orderStatus);
            return true;
        }).orElse(false);
    }
    
    public Optional<MsOrderStatus> getOrderStatusByName(String statusName) {
        return msOrderStatusRepository.findByStatusName(statusName);
    }
    
    public boolean existsByStatusName(String statusName) {
        return msOrderStatusRepository.existsByStatusName(statusName);
    }
}