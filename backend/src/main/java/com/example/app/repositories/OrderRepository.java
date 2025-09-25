package com.example.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.app.models.OrderModel;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, String> {
}
