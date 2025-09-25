package com.example.app.repositories;

import com.example.app.models.MsOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MsOrderStatusRepository extends JpaRepository<MsOrderStatus, Byte> {
    Optional<MsOrderStatus> findByStatusName(String statusName);
    Boolean existsByStatusName(String statusName);
}