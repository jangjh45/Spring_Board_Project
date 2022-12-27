package com.practice.app1.dao;

import com.practice.app1.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private SqlSession session;
    private static String namespace = "com.practice.app1.dao.UserMapper.";

    @Override
    public int insert(User user) throws Exception {
        return session.insert(namespace+"RegisterInsert", user);
    }

    @Override
    public User select(String id) {
        return session.selectOne(namespace+"LoginSelect", id);
    }
}