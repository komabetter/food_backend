package com.example.app.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.app.models.OrderModel;
import com.example.app.repositories.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public Page<OrderModel> getAllOrders(Pageable pageable, String status) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findByOrderStatusNameContainingIgnoreCase(status, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    public Optional<OrderModel> getOrderById(String id) {
        return  orderRepository.findById(id);
    }

    public OrderModel createOrder(OrderModel order) {
        return orderRepository.save(order);
    }

    public Optional<OrderModel> updateOrderStatus(String id, int statusId, String statusName) {
        Optional<OrderModel> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            OrderModel order = existingOrder.get();

            // Prevent cancellation after completion
            if (statusId == 5 && order.getOrderStatusId() == 4) {
                return Optional.empty();
            }

            // Allow only sequential progression (next status) or cancellation (status 5)
            // Valid transitions: current -> current+1, or any -> 5 (cancel)
            boolean isSequentialProgression = order.getOrderStatusId() + 1 == statusId;
            boolean isCancellation = statusId == 5 && order.getOrderStatusId() < 4;
            
            if (isSequentialProgression || isCancellation) {
                order.setOrderStatusId(statusId);
                order.setOrderStatusName(statusName);
                return Optional.of(orderRepository.save(order));
            }
        }
        return Optional.empty();
    }

    public boolean deleteOrder(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}