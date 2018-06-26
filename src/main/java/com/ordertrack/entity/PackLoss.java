package com.ordertrack.entity;

/* Created by AMXPC on 2018/6/17. */

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "packloss")
public class PackLoss {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "packlossid")
    private Integer id;

    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "packageid")
    private Integer packageId;

    @Column(name = "number")
    private Integer number;

}
