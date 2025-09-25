package com.example.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dtos.OrderDto;
import com.example.app.models.OrderModel;
import com.example.app.repositories.OrderRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public List<OrderDto> getOrders() {
        return orderRepository.getOrders().stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getOrderStatusId(),
                        order.getOrderStatusName(),
                        order.getOrderDetail(),
                        order.getPrice(),
                        order.getCustomerName()))
                .toList();
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody OrderDto orderDto) {

        orderRepository.addOrder(new OrderModel(
                orderDto.getOrderStatusId(),
                orderDto.getOrderStatusName(),
                orderDto.getOrderDetail(),
                orderDto.getPrice(),
                orderDto.getCustomerName()));
        return "entity";
    }

}