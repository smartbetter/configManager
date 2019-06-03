package com.example.server.manager.web.controller.home;

import com.example.server.manager.common.constants.CookieConstants;
import com.example.server.manager.common.constants.UrlConstants;
import com.example.server.manager.common.enums.ResultEnum;
import com.example.server.manager.common.protocol.ActionResult;
import com.example.server.manager.common.util.*;
import com.example.server.manager.dao.mysql.domain.User;
import com.example.server.manager.service.user.UserService;
import com.example.server.manager.web.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;

@Controller
@Slf4j
@PropertySource("classpath:config.properties")
public class LoginController extends BaseController {

    @Value("${COOKIE_DOMAIN}")
    private String cookieDomain;
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String loginRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            prepare(request, response, model, "loginRequest");
            return "home/screen/login";
        } catch (Throwable e) {
            log.error("LoginController.loginRequest error", e);
            return TemplateTool.exception(model, TemplateTool.COMMON_LAYOUT_DEFAULT_VM, new RuntimeException());
        }
    }

    @PostMapping("/json/user/login")
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response,
                        String erp, String password, String verifyCode, HttpSession session) {
        try {
            Cookie verifyCodeCookie = WebUtils.getCookie(request, CookieConstants.LOGIN_VERIFY_CODE);
            if (verifyCodeCookie == null || StringUtils.isBlank(verifyCodeCookie.getValue())) {
                return ResultTool.error(ResultEnum.ERROR, "graphic code.");
            }
            if (verifyCode.toLowerCase().equals(
                    AESUtil.decryptHex(verifyCodeCookie.getValue(), AESUtil.DEFAULT_ENCRYPT_KEY).toLowerCase())) {
                User user = userService.login(erp.trim(), password.trim());
                if (user == null) {
                    return ResultTool.error(ResultEnum.ERROR.getCode(), "userError");
                }
                // 单点session,分布式环境下可以考虑使用redis做分布式session
                session.setAttribute(String.valueOf(user.getId()), user);
                // 添加cookie
                CookieUtil.addCookie(response, CookieConstants.USER_ID,
                        AESUtil.encryptHex(user.getId().toString(), AESUtil.DEFAULT_ENCRYPT_KEY), cookieDomain);

                Cookie preUrlCookie = WebUtils.getCookie(request, CookieConstants.PRE_URL);

                ActionResult result = new ActionResult();
                if (preUrlCookie == null || StringUtils.isBlank(preUrlCookie.getValue())) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("http://").append(request.getServerName()).append(":").append(request.getServerPort());
                    result.addResult("preUrl", builder.toString());
                    return ResultTool.success(result);
                } else {
                    CookieUtil.removeCookie(request, response, CookieConstants.PRE_URL);
                    result.addResult("preUrl", URLDecoder.decode(preUrlCookie.getValue(), UrlConstants.ENCODE_UTF8));
                    return ResultTool.success(result);
                }
            }
            return ResultTool.error(ResultEnum.ERROR.getCode(), "verifyCodeError");
        } catch (Throwable e) {
            log.error("LoginController.login error", e);
            return ResultTool.exception(e);
        }
    }

    @RequestMapping("/json/user/logout")
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            // 注销session
            session.invalidate();
            CookieUtil.removeCookie(request, response, CookieConstants.USER_ID, cookieDomain);
            ActionResult result = new ActionResult();
            result.addResult("preUrl", request.getContextPath());
            return ResultTool.success(result);
        } catch (Throwable e) {
            log.error("LoginController.logout error", e);
            return ResultTool.exception(e);
        }
    }

    /**
     * 输出图形验证码
     */
    @RequestMapping("/login/verifyCodeImage")
    @ResponseBody
    public void verifyCodeImage(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(defaultValue = "105") int width,
                                @RequestParam(defaultValue = "35") int height) {
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");

            String verifyCode = AESUtil.encryptHex(VerifyCodeUtil
                    .generateVerifyCode(4), AESUtil.DEFAULT_ENCRYPT_KEY);

            CookieUtil.addCookie(response, CookieConstants.LOGIN_VERIFY_CODE, verifyCode);

            VerifyCodeUtil.outputImage(width, height, response.getOutputStream(),
                    AESUtil.decryptHex(verifyCode, AESUtil.DEFAULT_ENCRYPT_KEY));
        } catch (Throwable e) {
            log.error("LoginController.verifyCodeImage error", e);
        }
    }
}
