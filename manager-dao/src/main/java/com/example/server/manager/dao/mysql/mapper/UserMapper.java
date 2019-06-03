package com.example.server.manager.dao.mysql.mapper;

import com.example.server.manager.dao.mysql.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    User selectById(Long id);

    User selectByErpWithPassword(String erp);

    List<User> selectAll();

    int updateById(User user);

    int deleteById(Long id);
}
