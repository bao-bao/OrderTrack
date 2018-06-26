package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/12. */

import com.ordertrack.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
    List<Product> findByIsActive(boolean isActive);
}
