package com.example.app.services;

import java.util.List;
import java.util.Optional;

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

            if (statusId == 5 && order.getOrderStatusId() == 4) {
                return Optional.empty();
            }

            if (order.getOrderStatusId() < statusId) {
                order.setOrderStatusId(statusId);
                order.setOrderStatusName(statusName);
                return Optional.of(orderRepository.save(order));
            }

        }
        return Optional.empty();
    }
}