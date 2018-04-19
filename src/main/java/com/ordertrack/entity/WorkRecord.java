package com.ordertrack.entity;

/* Created by AMXPC on 2018/4/19. */

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "workrecord")
public class WorkRecord {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "workrecordid")
    private Integer id;

    @Column(name = "worker", length = 45)
    private String worker;

    @Column(name = "type")
    private Integer type;

    @Column(name = "orderdetail")
    private Integer orderDetail;

    @Column(name = "count")
    private Double count;

    @Column(name = "spand")
    private Double spand;

    @Column(name = "starttime")
    private Timestamp startTime;

    @Column(name = "isfinish")
    private Boolean isFinish;
}
