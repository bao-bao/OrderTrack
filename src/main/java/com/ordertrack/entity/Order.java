package com.ordertrack.entity;

/* Created by AMXPC on 2018/4/8. */

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "corder")
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "purchaseid", length = 45)
    private String purchaseId;

    @Column(name = "contractid", length = 45)
    private String contractId;

    @Column(name = "customname", length = 45)
    private String customName;

    @Column(name = "ordertime")
    private Timestamp orderTime;

    @Column(name = "totalweight")
    private Double totalWeight;

    @Column(name = "totalsmall")
    private Integer totalSmall;

    @Column(name = "totalbig")
    private Integer totalBig;

    @Column(name = "totalprice")
    private Double totalPrice;

    @Column(name = "deliverydate")
    private Timestamp deliveryDate;

    @Column(name = "picture")
    private String picture;

    @Column(name = "status")
    private Integer status;

    @Column(name = "changetime")
    private Timestamp changeTime;

    @Column(name = "createtime")
    private Timestamp createTime;

    @Column(name = "taketime")
    private Timestamp takeTime;

    @Column(name = "divisiontime")
    private Timestamp divisionTime;

    @Column(name = "pickuptime")
    private Timestamp pickUpTime;

    @Column(name = "checktime")
    private Timestamp checkTime;

    @Column(name = "cartime")
    private Timestamp carTime;

    @Column(name = "finishtime")
    private Timestamp finishTime;
}
