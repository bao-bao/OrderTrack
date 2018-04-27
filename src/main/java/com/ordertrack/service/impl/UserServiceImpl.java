package com.ordertrack.service.impl;

/* Created by AMXPC on 2018/3/26. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.dao.UserDao;
import com.ordertrack.entity.User;
import com.ordertrack.service.UserService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    @Override
    @Transactional
    public List<User> getUserList(int pageId, int pageSize, User filter) {
        Page<User> pageInfo = userDao.findAll(Example.of(filter), new PageRequest(pageId, pageSize, Sort.Direction.ASC, "userid"));
        ArrayList<User> userList = new ArrayList<>();
        for (User user : pageInfo) {
            userList.add(user);
        }
        return userList;
    }

    @Override
    @Transactional
    public ReturnCode loginValidate(String username, String password) {
        boolean isUserExist = (null != userDao.findUserByName(username));
        boolean isPasswordCorrect = (null != userDao.findUserByNameAndPassword(username, password));
        if (!isUserExist) {
            return ReturnCode.INVALID_USERNAME;
        }
        if (!isPasswordCorrect) {
            return ReturnCode.WRONG_PASSWORD;
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public User getUserInfo(String username) {
        return userDao.findUserByName(username);
    }

    @Override
    @Transactional
    public List<User> queryUserList(String username, Integer role) {
        if (username.equals("") && role == 0) {
            return userDao.findAll();
        } else if (!username.equals("") && role == 0) {
            return userDao.findByNameContainingOrderByRole(username);
        } else if (username.equals("") && role != 0) {
            return userDao.findByRoleOrderByRole(role);
        } else {
            return userDao.findByNameLikeAndRoleOrderByRole(username, role);
        }
    }

    @Override
    @Transactional
    public ReturnCode addUser(User user) {
        userDao.save(user);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateUser(User user) {
        userDao.saveAndFlush(user);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteUser(User user) {
        userDao.delete(user);
        return ReturnCode.SUCCESS;
    }
}
