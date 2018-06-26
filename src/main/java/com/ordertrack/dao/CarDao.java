package com.ordertrack.dao;

/* Created by AMXPC on 2018/5/17. */

import com.ordertrack.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface CarDao extends JpaRepository<Car, Long> {
    List<Car> findByOrderId(Integer orderId);
    int deleteByOrderIdAndLicense(Integer orderId, String license);
}
