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
    private Double standard;

    @Column(name = "type")
    private Integer type;

    @Column(name = "number")
    private Integer number;

    @Column(name = "isactive")
    private Boolean isActive;
}
