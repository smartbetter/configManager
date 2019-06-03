package com.example.server.manager.web.controller.base;

import com.example.server.manager.common.constants.CookieConstants;
import com.example.server.manager.dao.mysql.domain.User;
import com.example.server.manager.web.pulltool.RootContextPath;
import com.example.server.manager.web.pulltool.Tool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController {
    /**
     * 当前用户信息
     */
    protected User currentUser = null;
    /**
     * 操作人
     */
    protected String operator = null;
    /**
     * 操作人IP地址
     */
    protected String operatorAddress = null;
    /**
     * 服务环境
     */
    @Value("${server.env}")
    protected String serverEnv;

    @Autowired
    protected Tool tool;

    protected void prepare(HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getAttribute(CookieConstants.USER_ID);
        if (StringUtils.isNotBlank(userId)) {
            currentUser = (User) request.getSession().getAttribute(userId);
        }
        if (currentUser != null) {
            operator = currentUser.getErp();
        }
        operatorAddress = request.getRemoteAddr();
    }

    protected void prepare(HttpServletRequest request, HttpServletResponse response, Model model, String methodName) {
        prepare(request, response);
        model.addAttribute("_method", methodName);
        model.addAttribute("serverEnv", serverEnv);
        model.addAttribute("operator", operator);
        model.addAttribute("operatorAddress", operatorAddress);
        model.addAttribute("rootContextPath", new RootContextPath(request.getContextPath()));
        model.addAttribute("tool", tool);
    }
}
