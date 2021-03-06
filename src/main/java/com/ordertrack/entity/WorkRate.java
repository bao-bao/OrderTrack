package com.ordertrack.entity;

/* Created by AMXPC on 2018/4/11. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "workrate")
public class WorkRate {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "workrateid")
    private Integer id;

    @Column(name = "count")
    private Double count;

    @Column(name = "standard")
    private String standard;

    @Column(name = "isactive")
    private Boolean isActive;
}
