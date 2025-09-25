package com.example.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dtos.OrderDto;
import com.example.app.repositories.OrderRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class OrderController {

    @PostMapping("order")
    public String postMethodName(@RequestBody String entity) {

        return entity;
    }

    @GetMapping("/orders")
    public List<OrderDto> home() {
        OrderRepository orderRepo = new OrderRepository();
        return orderRepo.getOrders().stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getOrderStatusId(),
                        order.getOrderStatusName(),
                        order.getOrderDetail(),
                        order.getPrice(),
                        order.getCustomerName()))
                .toList();
    }
}