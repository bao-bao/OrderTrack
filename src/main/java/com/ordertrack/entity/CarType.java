package com.ordertrack.entity;

/* Created by AMXPC on 2018/5/29. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cartype")
public class CarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartypeid")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "isactive")
    private Boolean isActive;
}
