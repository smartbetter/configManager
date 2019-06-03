package com.example.server.manager.dao.mysql.domain;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
public class User {
    /**
     * 员工id
     */
    private Long id;
    /**
     * ERP账号
     */
    private String erp;
    /**
     * ERP密码
     */
    private String password;
    /**
     * 用户姓名
     */
    private String fullname;
    /**
     * 主动创建时间
     */
    private Date gmtCreate;
    /**
     * 被动更新时间
     */
    private Date gmtModified;
}
