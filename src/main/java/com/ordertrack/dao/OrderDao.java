package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long> {

    Order findByOrderId(Integer orderId);
    Order findOrderByContractId(String contractId);

    List<Order> findOrderByContractIdAndStatusLessThan(String contractId, Integer status);
    List<Order> findOrderByContractIdAndStatus(String contractId, Integer status);

    List<Order> findOrderByOrderTimeBetweenAndStatusLessThan(Timestamp startTime, Timestamp endTime, Integer status);
    List<Order> findOrderByOrderTimeBetweenAndStatus(Timestamp startTime, Timestamp endTime, Integer status);

    List<Order> findOrderByCustomNameContainingAndStatusLessThan(String customName, Integer status);
    List<Order> findOrderByCustomNameContainingAndStatus(String customName, Integer status);

    List<Order> findOrderByCustomNameContainingAndOrderTimeBetweenAndStatusLessThan(String customName, Timestamp startTime, Timestamp endTime, Integer status);
    List<Order> findOrderByCustomNameContainingAndOrderTimeBetweenAndStatus(String customName, Timestamp startTime, Timestamp endTime, Integer status);

    List<Order> findByStatusLessThan(Integer status);
    List<Order> findOrderByStatus(Integer status);
}
