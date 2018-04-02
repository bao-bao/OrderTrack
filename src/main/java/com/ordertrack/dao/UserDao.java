package com.ordertrack.dao;

/* Created by AMXPC on 2018/3/29. */

import com.ordertrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findUserByName(String username);
    User findUserByNameAndPassword(String username, String password);
}
