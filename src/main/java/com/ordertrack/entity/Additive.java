package com.ordertrack.entity;

/* Created by AMXPC on 2018/4/11. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "extra")
public class Additive {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "extraid")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "isactive")
    private Boolean isActive;
}
