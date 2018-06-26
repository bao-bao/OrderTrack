package com.ordertrack.dao;

/* Created by AMXPC on 2018/6/17. */

import com.ordertrack.entity.PackLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackLossDao extends JpaRepository<PackLoss, Long> {
    List<PackLoss> findByOrderId(Integer orderId);
}
