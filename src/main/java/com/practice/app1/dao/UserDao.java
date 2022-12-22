package com.practice.app1.dao;

import com.practice.app1.domain.User;

public interface UserDao {
    int insert(User user) throws Exception // int insert(String statement, Object parameter)
    ;
}
