package com.practice.app1.service;

import com.practice.app1.domain.User;

public interface UserService {
    int UserInsert(User user) throws Exception;

    User UserSelect(String id) throws Exception;
}