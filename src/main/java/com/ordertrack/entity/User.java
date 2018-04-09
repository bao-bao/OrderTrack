package com.ordertrack.entity;

/* Created by AMXPC on 2018/3/25. */
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer id;

    @Column(name = "username", length = 20)
    private String name;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "role")
    private Integer role;

    @Column(name = "isactive")
    private Boolean isActive;

    @Column(name = "createtime")
    private Timestamp createTime;

    @Column(name = "changetime")
    private Timestamp changeTime;
}