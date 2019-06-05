package com.example.server.soa.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 通用参数校验
 */
public class ApiValidator {

    /**
     * 批量校验参数是否为null
     *
     * @param paramMap 需要校验的一个或多个参数
     * @return 校验全部通过返回 "", 有参数校验失败则返回错误信息
     */
    public static String notEmptyValidator(Map<String, Object> paramMap) {
        StringBuilder builder = new StringBuilder();
        for (String key : paramMap.keySet()) {
            if (paramMap.get(key) == null) {
                builder.append(key).append(", ");
            }
        }
        if (StringUtils.isNotBlank(builder)) {
            return builder.append("cannot be empty").toString();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 非null非""且是数字
     *
     * @param cs
     * @return
     */
    public static boolean notBlankAndIsNumeric(final CharSequence cs) {
        return StringUtils.isNotBlank(cs) && StringUtils.isNumeric(cs);
    }

    /**
     * null或""或非数字
     *
     * @param cs
     * @return
     */
    public static boolean isBlankOrNotNumeric(final CharSequence cs) {
        return !notBlankAndIsNumeric(cs);
    }
}
