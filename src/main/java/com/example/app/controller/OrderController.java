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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @PostMapping("/orders")
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {

        OrderModel createdOrder = orderRepository.addOrder(new OrderModel(
                orderDto.getOrderStatusId(),
                orderDto.getOrderStatusName(),
                orderDto.getOrderDetail(),
                orderDto.getPrice(),
                orderDto.getCustomerName()));

        return new OrderDto(
                createdOrder.getId(),
                createdOrder.getOrderStatusId(),
                createdOrder.getOrderStatusName(),
                createdOrder.getOrderDetail(),
                createdOrder.getPrice(),
                createdOrder.getCustomerName());
    }

    @PutMapping("/order/{id}")
    public String updateOrder(@PathVariable String id, @RequestBody OrderDto orderDto) {
        
        OrderModel updateOrder = new OrderModel(id,orderDto.getOrderStatusId(),orderDto.getOrderStatusName());
        orderRepository.editOrder(updateOrder);

        return "Update Success";
    }

}