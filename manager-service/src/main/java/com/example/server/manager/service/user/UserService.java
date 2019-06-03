package com.example.server.manager.service.user;

import com.example.server.manager.dao.mysql.domain.User;

public interface UserService {
    User login(String erp, String password);
}
