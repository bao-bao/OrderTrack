package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/11. */

import com.ordertrack.entity.Additive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditiveDao extends JpaRepository<Additive, Long> {
    List<Additive> findByIsActive(boolean isActive);
}
