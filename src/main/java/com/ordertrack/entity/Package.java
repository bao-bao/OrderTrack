package com.ordertrack.entity;

/* Created by AMXPC on 2018/5/8. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "package")
public class Package {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "packageid")
    private Integer id;

    @Column(name = "standard")
    private String standard;

    @Column(name = "type")
    private Integer type;

    @Column(name = "price")
    private Double price;

    @Column(name = "number")
    private Integer number;

    @Column(name = "isactive")
    private Boolean isActive;
}
