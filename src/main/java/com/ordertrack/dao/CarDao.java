package com.ordertrack.dao;

/* Created by AMXPC on 2018/5/17. */

import com.ordertrack.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarDao extends JpaRepository<Car, Long> {
    List<Car> findByOrderId(Integer orderId);
}
