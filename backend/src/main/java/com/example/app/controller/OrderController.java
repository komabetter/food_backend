package com.example.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dtos.CreateOrderRequest;
import com.example.app.dtos.ErrorResponse;
import com.example.app.dtos.OrderDto;
import com.example.app.dtos.PagedOrderResponse;
import com.example.app.dtos.SuccessResponse;
import com.example.app.dtos.UpdateOrderStatusRequest;
import com.example.app.models.OrderModel;
import com.example.app.services.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Create Order (ID requires UUID)
    @PostMapping("/orders")
    public ResponseEntity<SuccessResponse<OrderDto>> createOrder(@RequestBody CreateOrderRequest request) {
        OrderModel order = new OrderModel(
                request.getOrderStatusId(),
                request.getOrderStatusName(),
                request.getOrderDetail(),
                request.getPrice(),
                request.getCustomerName());

        OrderModel createdOrder = orderService.createOrder(order);

        OrderDto orderDto = new OrderDto(
                createdOrder.getId(),
                createdOrder.getOrderStatusId(),
                createdOrder.getOrderStatusName(),
                createdOrder.getOrderDetail(),
                createdOrder.getPrice(),
                createdOrder.getCustomerName());

        SuccessResponse<OrderDto> response = new SuccessResponse<>("200", orderDto, "SUCCESS");
        return ResponseEntity.ok(response);
    }

    // 2. Update Order (only statusId, statusName)
    @PutMapping("/orders/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String id, @RequestBody UpdateOrderStatusRequest request) {
        Optional<OrderModel> updatedOrder = orderService.updateOrderStatus(id, request.getStatusId(),
                request.getStatusName());

        if (updatedOrder.isPresent()) {
            OrderModel order = updatedOrder.get();
            OrderDto orderDto = new OrderDto(
                    order.getId(),
                    order.getOrderStatusId(),
                    order.getOrderStatusName(),
                    order.getOrderDetail(),
                    order.getPrice(),
                    order.getCustomerName());
            SuccessResponse<OrderDto> response = new SuccessResponse<>("200", orderDto, "SUCCESS");
            return ResponseEntity.ok(response);
        }

        ErrorResponse errorResponse = new ErrorResponse("400", "FAILD");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 3. Get Order by ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Optional<OrderModel> order = orderService.getOrderById(id);

        if (order.isPresent()) {
            OrderModel orderModel = order.get();
            OrderDto orderDto = new OrderDto(
                    orderModel.getId(),
                    orderModel.getOrderStatusId(),
                    orderModel.getOrderStatusName(),
                    orderModel.getOrderDetail(),
                    orderModel.getPrice(),
                    orderModel.getCustomerName());
            SuccessResponse<OrderDto> response = new SuccessResponse<>("200", orderDto, "SUCCESS");
            return ResponseEntity.ok(response);
        }

        ErrorResponse errorResponse = new ErrorResponse("400", "Invalid Status Code");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 4. Get Orders (List with Pagination)
    @GetMapping("/orders")
    public ResponseEntity<SuccessResponse<PagedOrderResponse>> getAllOrdersPaginated(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String status) {
        
        Page<OrderModel> ordersPage = orderService.getAllOrders(pageable, status);

        List<OrderDto> orderDtos = ordersPage.getContent().stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getOrderStatusId(),
                        order.getOrderStatusName(),
                        order.getOrderDetail(),
                        order.getPrice(),
                        order.getCustomerName()))
                .toList();

        PagedOrderResponse pagedResponse = new PagedOrderResponse(
                orderDtos,
                ordersPage.getNumber(),
                ordersPage.getSize(),
                ordersPage.getTotalElements(),
                ordersPage.getTotalPages(),
                ordersPage.isFirst(),
                ordersPage.isLast(),
                ordersPage.hasNext(),
                ordersPage.hasPrevious()
        );

        SuccessResponse<PagedOrderResponse> response = new SuccessResponse<>("200", pagedResponse, "SUCCESS");
        return ResponseEntity.ok(response);
    }

    // 5. Delete Order
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        boolean deleted = orderService.deleteOrder(id);
        
        if (deleted) {
            SuccessResponse<String> response = new SuccessResponse<>("200", "Order deleted successfully", "SUCCESS");
            return ResponseEntity.ok(response);
        }
        
        ErrorResponse errorResponse = new ErrorResponse("400", "Order not found or could not be deleted");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}