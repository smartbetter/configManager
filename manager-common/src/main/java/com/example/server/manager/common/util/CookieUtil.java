package com.example.server.manager.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date 2017-10-01
 */
public class CookieUtil {

    private CookieUtil() {
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param domain
     * @param maxage      cookie最长存活时间 单位秒
     * @param path
     */
    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, String domain, int maxage, String path) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        // 默认-1, 表示关闭浏览器, cookie就会消失
        cookie.setMaxAge(maxage);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, String domain, String path) {
        addCookie(response, cookieName, cookieValue, domain, -1, path);
    }

    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, int maxage, String path) {
        addCookie(response, cookieName, cookieValue, null, maxage, path);
    }

    /**
     * 往根路径下存一个cookie
     *
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param domain
     * @param maxage      cookie最长存活时间 单位秒
     */
    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, String domain, int maxage) {
        addCookie(response, cookieName, cookieValue, domain, maxage, "/");
    }

    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, String domain) {
        addCookie(response, cookieName, cookieValue, domain, -1, "/");
    }

    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue, int maxage) {
        addCookie(response, cookieName, cookieValue, null, maxage, "/");
    }

    public static void addCookie(HttpServletResponse response,
                                 String cookieName, String cookieValue) {
        addCookie(response, cookieName, cookieValue, null, -1, "/");
    }

    /**
     * 从cookie中得到cookie值,如果没有返回null
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(cookieName)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 根据名称获取cookie对象
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(cookieName)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 清除某个域名下的cookie
     *
     * @param request
     * @param response
     * @param cookieName
     * @param domain
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName, String domain) {
        String cookieValue = getCookieValue(request, cookieName);
        if (StringUtils.isNotBlank(cookieValue)) {
            addCookie(response, cookieName, null, domain, 0);
        }
    }

    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        String cookieValue = getCookieValue(request, cookieName);
        if (StringUtils.isNotBlank(cookieValue)) {
            addCookie(response, cookieName, null, 0);
        }
    }

    /**
     * 取客户端存入header中cookie
     *
     * @param request
     * @return
     */
    public static String getCookieFromHeader(HttpServletRequest request) {
        return request.getHeader("Cookie");
    }
}
