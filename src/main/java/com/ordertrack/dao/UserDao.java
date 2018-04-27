package com.ordertrack.dao;

/* Created by AMXPC on 2018/3/29. */

import com.ordertrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findUserByName(String username);
    User findUserByNameAndPassword(String username, String password);
    List<User> findByNameContainingOrderByRole(String username);
    List<User> findByRoleOrderByRole(Integer role);
    List<User> findByNameLikeAndRoleOrderByRole(String username, Integer role);
}
