package com.ordertrack.entity;

/* Created by AMXPC on 2018/4/9. */

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "orderdetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "orderdetailid")
    private Integer id;

    @Column(name = "orderid")
    private Integer orderId;

    @Column(name = "chinesename", length = 45)
    private String chineseName;

    @Column(name = "productweight")
    private Double productWeight;

    @Column(name = "productstandard")
    private String productStandard;

    @Column(name = "innerstandard")
    private String innerStandard;

    @Column(name = "innercount")
    private Integer innerCount;

    @Column(name = "outerstandard")
    private String outerStandard;

    @Column(name = "outercount")
    private Integer outerCount;

    @Column(name = "smell")
    private String smell;

    @Column(name = "fruitsticker")
    private String fruitSticker;

    @Column(name = "extra", length = 200)
    private String extra;

    @Column(name = "remark", length = 200)
    private String remark;
    
    @Column(name = "changetime")
    private Timestamp changeTime;

    @Column(name = "createtime")
    private Timestamp createTime;
}
