package com.example.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.app.models.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {
    Page<OrderModel> findByOrderStatusNameContainingIgnoreCase(String status, Pageable pageable);
}
