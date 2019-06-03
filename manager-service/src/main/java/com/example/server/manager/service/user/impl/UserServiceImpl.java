package com.example.server.manager.service.user.impl;

import com.example.server.manager.common.util.MD5Util;
import com.example.server.manager.dao.mysql.domain.User;
import com.example.server.manager.dao.mysql.mapper.UserMapper;
import com.example.server.manager.service.base.BaseService;
import com.example.server.manager.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String erp, String password) {
        if (StringUtils.isBlank(erp) || StringUtils.isBlank(password)) {
            return null;
        }
        User user = userMapper.selectByErpWithPassword(erp);
        if (user == null) {
            return null;
        }
        if (MD5Util.md5Hex(password).equals(user.getPassword())) {
            user.setPassword(null);
            return user;
        }
        return null;
    }
}
