package com.example.server.manager.common.exception;

/**
 * 所有的服务异常都统一封装成这个类
 *
 * @date 2018-06-12
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 8702250771839823062L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
