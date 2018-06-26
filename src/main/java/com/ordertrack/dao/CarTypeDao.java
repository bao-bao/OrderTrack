package com.ordertrack.dao;

/* Created by AMXPC on 2018/5/29. */

import com.ordertrack.entity.CarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarTypeDao extends JpaRepository<CarType, Long> {
    List<CarType> findByIsActive(boolean isActive);
}
