package com.practice.app1.service;

import com.practice.app1.dao.UserDao;
import com.practice.app1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public int UserInsert(User user) throws Exception {
        return userDao.insert(user);
    }

    @Override
    public User UserSelect(String id) throws Exception {
        return userDao.select(id);
    }
}
