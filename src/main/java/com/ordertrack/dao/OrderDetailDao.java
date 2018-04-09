package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/9. */

import com.ordertrack.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Integer orderId);
    int deleteByOrderId(Integer orderId);
}
