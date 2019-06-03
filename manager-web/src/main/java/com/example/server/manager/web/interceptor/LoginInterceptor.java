package com.example.server.manager.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.server.manager.common.util.AESUtil;
import com.example.server.manager.common.util.CookieUtil;
import com.example.server.manager.common.constants.CookieConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

/**
 * 登录拦截器
 */
@Slf4j
@PropertySource("classpath:config.properties")
public class LoginInterceptor implements HandlerInterceptor {

    private static final String REQUEST_URI_LOGIN = "/login";

    @Value("${COOKIE_DOMAIN}")
    private String cookieDomain;

    /**
     * step 1、在Controller处理请求之前被调用
     *
     * @param handler 被拦截请求对象实例
     * @return        false-表示拦截当前请求,请求被终止, true-表示不拦截当前请求,请求被继续
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());

        // 判断cookie中是否包含USER_ID
        Cookie userIdCookie = WebUtils.getCookie(request, CookieConstants.USER_ID);
        if (userIdCookie != null && StringUtils.isNotBlank(userIdCookie.getValue())) {
            // 获取USER_ID、USER_ERP
            String userId = AESUtil.decryptHex(userIdCookie.getValue(), AESUtil.DEFAULT_ENCRYPT_KEY);
            if (request.getSession().getAttribute(userId) != null) {
                // 登录状态
                request.setAttribute(CookieConstants.USER_ID, userId);
                return true;
            }
        }

        // 未登录状态
        // 登录成功后跳转的地址
        String preUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            preUrl = preUrl +  "?" + queryString;
        }
        CookieUtil.addCookie(response, CookieConstants.PRE_URL, preUrl);
        // 进入登陆界面
        response.sendRedirect(REQUEST_URI_LOGIN);
        return false;
    }

    /**
     * step 2、在Controller处理请求之后被调用,生成视图之前执行的动作
     *
     * @param handler      被拦截请求对象实例
     * @param modelAndView 可通过modelAndView改变显示的视图或修改发往视图的方法,比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * step 3、在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     *
     * 注意: 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion方法
     * @param handler 被拦截请求对象实例
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception e) throws Exception {
        Long start = (Long) request.getAttribute("startTime");
        log.info("本次请求耗时: {}", (System.currentTimeMillis()-start));
    }
}
