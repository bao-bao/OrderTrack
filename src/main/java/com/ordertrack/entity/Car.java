package com.ordertrack.entity;

/* Created by AMXPC on 2018/5/17. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "carid")
    private Integer id;

    @Column(name = "license")
    private String license;

    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "count")
    private Integer count;

    @Column(name = "standardid")
    private Integer standardId;
}
