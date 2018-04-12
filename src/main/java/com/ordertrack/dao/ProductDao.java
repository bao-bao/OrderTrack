package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/12. */

import com.ordertrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findByIsActive(boolean isActive);
}
