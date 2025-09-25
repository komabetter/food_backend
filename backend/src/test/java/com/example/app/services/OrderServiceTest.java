package com.example.app.services;

import com.example.app.models.OrderModel;
import com.example.app.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderModel testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new OrderModel(
            "test-order-id",
            1,
            "Pending",
            "Test order details",
            25.99f,
            "John Doe"
        );
    }

    @Test
    @DisplayName("Should successfully update order status when valid forward progression")
    void updateOrderStatus_ValidForwardProgression_ReturnsUpdatedOrder() {
        // Given
        OrderModel existingOrder = new OrderModel(
            "test-order-id", 1, "Pending", "Test order details", 25.99f, "John Doe"
        );
        OrderModel updatedOrder = new OrderModel(
            "test-order-id", 2, "Confirmed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(updatedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 2, "Confirmed");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(2);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Confirmed");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when trying to revert order status (backward progression)")
    void updateOrderStatus_BackwardProgression_ReturnsEmpty() {
        // Given
        OrderModel existingOrder = new OrderModel(
            "test-order-id", 3, "In Progress", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(existingOrder));

        // When - trying to revert from status 3 to status 1
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 1, "Pending");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when trying to set same status (no progression)")
    void updateOrderStatus_SameStatus_ReturnsEmpty() {
        // Given
        OrderModel existingOrder = new OrderModel(
            "test-order-id", 2, "Confirmed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(existingOrder));

        // When - trying to set the same status
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 2, "Confirmed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when trying to cancel after completion (status 5 after status 4)")
    void updateOrderStatus_CancelAfterCompletion_ReturnsEmpty() {
        // Given
        OrderModel completedOrder = new OrderModel(
            "test-order-id", 4, "Completed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(completedOrder));

        // When - trying to cancel after completion
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 5, "Cancelled");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when order does not exist")
    void updateOrderStatus_OrderNotFound_ReturnsEmpty() {
        // Given
        when(orderRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("non-existent-id", 2, "Confirmed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow cancellation before completion")
    void updateOrderStatus_CancelBeforeCompletion_ReturnsUpdatedOrder() {
        // Given - order at status 2, cancelling to status 5 (allowed before completion)
        OrderModel existingOrder = new OrderModel(
            "test-order-id", 2, "Confirmed", "Test order details", 25.99f, "John Doe"
        );
        OrderModel cancelledOrder = new OrderModel(
            "test-order-id", 5, "Cancelled", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(cancelledOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 5, "Cancelled");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(5);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Cancelled");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential progression from status 2 to 3")
    void updateOrderStatus_Status2To3_ReturnsUpdatedOrder() {
        // Given
        OrderModel confirmedOrder = new OrderModel(
            "test-order-id", 2, "Confirmed", "Test order details", 25.99f, "John Doe"
        );
        OrderModel inProgressOrder = new OrderModel(
            "test-order-id", 3, "In Progress", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(confirmedOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(inProgressOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 3, "In Progress");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(3);
        assertThat(result.get().getOrderStatusName()).isEqualTo("In Progress");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential progression from status 3 to 4")
    void updateOrderStatus_Status3To4_ReturnsUpdatedOrder() {
        // Given
        OrderModel inProgressOrder = new OrderModel(
            "test-order-id", 3, "In Progress", "Test order details", 25.99f, "John Doe"
        );
        OrderModel completedOrder = new OrderModel(
            "test-order-id", 4, "Completed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(inProgressOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(completedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 4, "Completed");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(4);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Completed");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential status progression (1->2)")
    void updateOrderStatus_SequentialProgression_AllowsProgression() {
        // Given - order at status 1, progressing to status 2 (sequential)
        OrderModel pendingOrder = new OrderModel(
            "test-order-id", 1, "Pending", "Test order details", 25.99f, "John Doe"
        );
        OrderModel confirmedOrder = new OrderModel(
            "test-order-id", 2, "Confirmed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(confirmedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 2, "Confirmed");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(2);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Confirmed");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should prevent skipping status levels")
    void updateOrderStatus_SkipStatusLevels_ReturnsEmpty() {
        // Given - order at status 1, trying to jump to status 3 (should only allow 1->2)
        OrderModel existingOrder = new OrderModel(
            "test-order-id", 1, "Pending", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(existingOrder));

        // When - trying to skip from status 1 to status 3
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 3, "In Progress");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should handle edge case: trying to update completed order to completed")
    void updateOrderStatus_CompletedToCompleted_ReturnsEmpty() {
        // Given
        OrderModel completedOrder = new OrderModel(
            "test-order-id", 4, "Completed", "Test order details", 25.99f, "John Doe"
        );

        when(orderRepository.findById("test-order-id")).thenReturn(Optional.of(completedOrder));

        // When - trying to set completed order to completed again
        Optional<OrderModel> result = orderService.updateOrderStatus("test-order-id", 4, "Completed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }
}