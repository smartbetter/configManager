package com.example.server.manager.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.ui.Model;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @date 2018-10-01
 */
public class TemplateTool {
    /**
     * 布局器 空白
     */
    public static final String DEFAULT_VM = "default.vm";
    /**
     * 布局器 带基础HTML代码
     */
    public static final String COMMON_LAYOUT_DEFAULT_VM = "common/layout/default.vm";
    /**
     * 布局器 带基础HTML代码、带头部区域和左侧导航区域
     */
    public static final String HOME_LAYOUT_DEFAULT_VM = "home/layout/default.vm";
    /**
     * 错误页面模板
     */
    private static final String COMMON_SCREEN_ERROR_OTHER = "common/screen/error_other";
    /**
     * 无权限页面模板
     */
    private static final String COMMON_SCREEN_ILLEGAL = "common/screen/illegal";

    /**
     * 处理模板引擎错误的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;RequestMapping("/index")
     * public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
     *     try { ...
     *         return ResultTool.error(model, TemplateTool.DEFAULT_VM, message);
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param model
     * @param layoutVm 布局器
     * @param message  错误信息
     * @return String
     */
    public static String error(Model model, String layoutVm, String message) {
        return error(model, layoutVm, message, "None");
    }

    /**
     * 处理模板引擎错误的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;RequestMapping("/index")
     * public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
     *     try { ...
     *         return ResultTool.error(model, TemplateTool.DEFAULT_VM, message, detailMessage);
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param model
     * @param layoutVm      布局器
     * @param message       错误信息
     * @param detailMessage
     * @return String
     */
    public static String error(Model model, String layoutVm, String message, String detailMessage) {
        if (StringUtils.isBlank(layoutVm)) {
            layoutVm = DEFAULT_VM;
        }
        model.addAttribute("layoutVm", layoutVm);
        StringBuilder builder = new StringBuilder();
        model.addAttribute("errorMsg", builder.append("ERROR MSG: ").append(message).toString());
        model.addAttribute("errorDetailMsg", StringUtils.isBlank(detailMessage) ?
                "Detail message is null" : detailMessage);
        return COMMON_SCREEN_ERROR_OTHER;
    }

    /**
     * 处理模板引擎异常的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;RequestMapping("/index")
     * public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
     *     try {
     *         ...
     *     } catch (Throwable e) {
     *         log.error("Controller.indexRequest error", e);
     *         return TemplateTool.exception(model, TemplateTool.DEFAULT_VM, e);
     *     }
     * }
     * </pre>
     *
     * @param model
     * @param layoutVm 布局器
     * @param e
     * @return String
     */
    public static String exception(Model model, String layoutVm, Throwable e) {
        if (StringUtils.isBlank(layoutVm)) {
            layoutVm = DEFAULT_VM;
        }
        model.addAttribute("layoutVm", layoutVm);
        StringBuilder builder = new StringBuilder();
        model.addAttribute("errorMsg", e == null ?
                "Throwable is null" : builder.append("EXCEPTION MSG: ").append(e.getMessage()).toString());
        StringBuilder detailBuilder = new StringBuilder();
        //需要对异常堆栈信息进行转义
        model.addAttribute("errorDetailMsg", e == null ?
                "Throwable is null" : StringEscapeUtils.escapeJava(detailBuilder.append(e2String(e)).toString()));
        return COMMON_SCREEN_ERROR_OTHER;
    }

    /**
     * 处理无权限的返回结果
     * <p>
     * Controller:
     * <pre>
     * &#64;RequestMapping("/index")
     * public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
     *     try { ...
     *         return ResultTool.illegal(model, TemplateTool.DEFAULT_VM);
     *     } catch (Throwable e) { ...
     *     }
     * }
     * </pre>
     *
     * @param model
     * @param layoutVm 布局器
     * @return String
     */
    public static String illegal(Model model, String layoutVm) {
        if (StringUtils.isBlank(layoutVm)) {
            layoutVm = DEFAULT_VM;
        }
        model.addAttribute("layoutVm", layoutVm);
        return COMMON_SCREEN_ILLEGAL;
    }

    private static String e2String(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}
