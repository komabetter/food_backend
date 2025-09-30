package com.example.app.services;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.app.models.OrderModel;
import com.example.app.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private static final String TEST_ORDER_ID = "test-order-id";
    private static final int STATUS_PENDING = 1;
    private static final int STATUS_CONFIRMED = 2;
    private static final int STATUS_IN_PROGRESS = 3;
    private static final int STATUS_COMPLETED = 4;
    private static final int STATUS_CANCELLED = 5;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderModel createOrder(int statusId, String statusName) {
        return new OrderModel(
                TEST_ORDER_ID,
                statusId,
                statusName,
                "Test order details",
                25.99f,
                "John Doe"
        );
    }

    @Test
    @DisplayName("Should successfully update order status when valid forward progression")
    void updateOrderStatus_ValidForwardProgression_ReturnsUpdatedOrder() {
        // Given
        OrderModel existingOrder = createOrder(STATUS_PENDING, "Pending");
        OrderModel confirmedOrder = createOrder(STATUS_CONFIRMED, "Confirmed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(confirmedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_CONFIRMED, "Confirmed");

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
        OrderModel existingOrder = createOrder(STATUS_IN_PROGRESS, "In Progress");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));

        // When - trying to revert from status 3 to status 1
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_PENDING, "Pending");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when trying to set same status (no progression)")
    void updateOrderStatus_SameStatus_ReturnsEmpty() {
        // Given
        OrderModel existingOrder = createOrder(STATUS_CONFIRMED, "Confirmed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));

        // When - trying to set the same status
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_CONFIRMED, "Confirmed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should fail when trying to cancel after completion (status 5 after status 4)")
    void updateOrderStatus_CancelAfterCompletion_ReturnsEmpty() {
        // Given
        OrderModel completedOrder = createOrder(STATUS_COMPLETED, "Completed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(completedOrder));

        // When - trying to cancel after completion
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_CANCELLED, "Cancelled");

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
        Optional<OrderModel> result = orderService.updateOrderStatus("non-existent-id", STATUS_CONFIRMED, "Confirmed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow cancellation before completion")
    void updateOrderStatus_CancelBeforeCompletion_ReturnsUpdatedOrder() {
        // Given - order at status 2, cancelling to status 5 (allowed before completion)
        OrderModel existingOrder = createOrder(STATUS_CONFIRMED, "Confirmed");
        OrderModel cancelledOrder = createOrder(STATUS_CANCELLED, "Cancelled");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(cancelledOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_CANCELLED, "Cancelled");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(STATUS_CANCELLED);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Cancelled");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential progression from status 2 to 3")
    void updateOrderStatus_Status2To3_ReturnsUpdatedOrder() {
        // Given
        OrderModel confirmedOrder = createOrder(STATUS_CONFIRMED, "Confirmed");
        OrderModel inProgressOrder = createOrder(STATUS_IN_PROGRESS, "In Progress");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(confirmedOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(inProgressOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_IN_PROGRESS, "In Progress");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(STATUS_IN_PROGRESS);
        assertThat(result.get().getOrderStatusName()).isEqualTo("In Progress");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential progression from status 3 to 4")
    void updateOrderStatus_Status3To4_ReturnsUpdatedOrder() {
        // Given
        OrderModel inProgressOrder = createOrder(STATUS_IN_PROGRESS, "In Progress");
        OrderModel completedOrder = createOrder(STATUS_COMPLETED, "Completed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(inProgressOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(completedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_COMPLETED, "Completed");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(STATUS_COMPLETED);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Completed");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should allow sequential status progression (1->2)")
    void updateOrderStatus_SequentialProgression_AllowsProgression() {
        // Given - order at status 1, progressing to status 2 (sequential)
        OrderModel pendingOrder = createOrder(STATUS_PENDING, "Pending");
        OrderModel confirmedOrder = createOrder(STATUS_CONFIRMED, "Confirmed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(OrderModel.class))).thenReturn(confirmedOrder);

        // When
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_CONFIRMED, "Confirmed");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderStatusId()).isEqualTo(STATUS_CONFIRMED);
        assertThat(result.get().getOrderStatusName()).isEqualTo("Confirmed");
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should prevent skipping status levels")
    void updateOrderStatus_SkipStatusLevels_ReturnsEmpty() {
        // Given - order at status 1, trying to jump to status 3 (should only allow 1->2)
        OrderModel existingOrder = createOrder(STATUS_PENDING, "Pending");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(existingOrder));

        // When - trying to skip from status 1 to status 3
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_IN_PROGRESS, "In Progress");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }

    @Test
    @DisplayName("Should handle edge case: trying to update completed order to completed")
    void updateOrderStatus_CompletedToCompleted_ReturnsEmpty() {
        // Given
        OrderModel completedOrder = createOrder(STATUS_COMPLETED, "Completed");

        when(orderRepository.findById(TEST_ORDER_ID)).thenReturn(Optional.of(completedOrder));

        // When - trying to set completed order to completed again
        Optional<OrderModel> result = orderService.updateOrderStatus(TEST_ORDER_ID, STATUS_COMPLETED, "Completed");

        // Then
        assertThat(result).isEmpty();
        verify(orderRepository, never()).save(any(OrderModel.class));
    }
}
