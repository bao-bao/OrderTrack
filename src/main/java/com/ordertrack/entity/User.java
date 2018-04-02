package com.ordertrack.entity;

/* Created by AMXPC on 2018/3/25. */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
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

}