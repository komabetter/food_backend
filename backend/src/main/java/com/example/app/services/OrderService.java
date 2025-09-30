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

            // Handle special cases for cancellation (status 5)
            if (statusId == 5) {
                // Allow cancellation only before completion (before status 4)
                if (order.getOrderStatusId() >= 4) {
                    return Optional.empty(); // Cannot cancel after completion
                }
            } else if (statusId == 6) {
                if (order.getOrderStatusId() >= 1 && order.getOrderStatusId() <= 4) {
                } else {
                    return Optional.empty(); 
                }
            } else {
                // Regular sequential progression
                if (statusId != order.getOrderStatusId() + 1) {
                    return Optional.empty();
                }
            }

            order.setOrderStatusId(statusId);
            order.setOrderStatusName(statusName);
            return Optional.of(orderRepository.save(order));
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