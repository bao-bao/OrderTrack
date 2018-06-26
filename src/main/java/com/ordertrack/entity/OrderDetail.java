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

    @Column(name = "piecepirce")
    private Double piecePrice;

    @Column(name = "productprice")
    private Double productPrice;

    @Column(name = "productweight")
    private Double productWeight;

    @Column(name = "productstandard")
    private String productStandard;

    @Column(name = "innerstandard")
    private Integer innerStandard;

    @Column(name = "innercount")
    private Integer innerCount;

    @Column(name = "outerstandard")
    private Integer outerStandard;

    @Column(name = "outercount")
    private Integer outerCount;

    @Column(name = "smell")
    private String smell;

    @Column(name = "fruitsticker")
    private String fruitSticker;

    @Column(name = "packtype")
    private Integer packType;

    @Column(name = "extra", length = 200)
    private String extra;

    @Column(name = "remark", length = 200)
    private String remark;

    @Column(name = "isfinish")
    private Boolean isFinish;

    @Column(name = "carprice")
    private Double carPrice;

    @Column(name = "packageprice")
    private Double packagePrice;

    @Column(name = "changetime")
    private Timestamp changeTime;

    @Column(name = "createtime")
    private Timestamp createTime;
}
