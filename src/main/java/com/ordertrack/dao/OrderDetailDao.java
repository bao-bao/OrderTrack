package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/9. */

import com.ordertrack.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Integer orderId);
    OrderDetail findById(Integer id);
    int deleteByOrderId(Integer orderId);

    @Modifying
    @Query("update OrderDetail od set od.isFinish = true where od.id = :detailId")
    void updateIsFinishByOrderDetailId(@Param(value = "detailId") Integer detailId);
}
