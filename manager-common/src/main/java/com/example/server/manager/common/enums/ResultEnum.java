package com.example.server.manager.common.enums;

/**
 * 返回码枚举
 *
 * @date 2017-10-01
 */
public enum ResultEnum {
    /**
     * 调用成功
     */
    SUCCESS("0"),
    /**
     * 调用失败
     */
    ERROR("1", "error"),
    /**
     * 参数错误
     */
    PARAM_ERROR("2", "param error"),
    /**
     * 未经授权
     */
    UNAUTHORIZED("3", "unauthorized"),
    /**
     * 系统异常
     */
    EXCEPTION("-1", "exception"),
    /**
     * 命中排队
     */
    QUEUING("601", "queuing");

    private String code;

    private String msg;

    ResultEnum(String code) {
        this.code = code;
    }

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
