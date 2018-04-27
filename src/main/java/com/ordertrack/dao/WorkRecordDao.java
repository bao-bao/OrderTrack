package com.ordertrack.dao;

/* Created by AMXPC on 2018/4/19. */

import com.ordertrack.entity.WorkRecord;
import org.omg.CORBA.INTERNAL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRecordDao extends JpaRepository<WorkRecord, Long> {
    List<WorkRecord> findByOrderDetail(int detailId);
    int deleteByOrderDetail(Integer detailId);

    @Modifying
    @Query("update WorkRecord wr set wr.isFinish = true where wr.orderDetail = :orderDetail")
    void updateIsFinishByOrderDetailId(@Param(value = "orderDetail") Integer orderDetail);

    @Query(value = "select * from otrack.workrecord where worker=?1 and year(starttime) = ?2 and month(starttime) = ?3", nativeQuery = true)
    List<WorkRecord> findWorkRecordByWorkerAndTime(String name, Integer year, Integer month);

    List<WorkRecord> findWorkRecordByWorker(String name);

    @Query(value = "select * from otrack.workrecord where year(starttime) = ?1 and month(starttime) = ?2", nativeQuery = true)
    List<WorkRecord> findWorkRecordByTime(Integer year, Integer month);
}
