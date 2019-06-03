package com.example.server.manager.web.interceptor;

import com.example.server.manager.common.constants.CookieConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限验证拦截器
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private static final String REQUEST_URI_ILLEGAL = "/illegal.html";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (checkPermission(request)) {
            return true;
        }
        // 进入无权限界面
        response.sendRedirect(REQUEST_URI_ILLEGAL);
        return false;
    }

    /**
     * 系统总的权限开关
     *
     * @param request
     * @return true:权限验证通过, false:权限验证不通过
     */
    private boolean checkPermission(HttpServletRequest request) {
        //TODO 权限控制逻辑
        String userId = (String) request.getAttribute(CookieConstants.USER_ID);

        return true;
    }
}
