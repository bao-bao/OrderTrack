package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/19. */

import com.ordertrack.entity.WorkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRecordDao extends JpaRepository<WorkRecord, Long> {
    List<WorkRecord> findByOrderDetail(int detailId);
    int deleteByOrderDetail(Integer detailId);
}
