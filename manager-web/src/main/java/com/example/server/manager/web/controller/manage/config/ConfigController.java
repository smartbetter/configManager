package com.example.server.manager.web.controller.manage.config;

import com.example.server.manager.common.util.TemplateTool;
import com.example.server.manager.service.config.ConfigService;
import com.example.server.manager.web.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class ConfigController extends BaseController {

    @Autowired
    private ConfigService configService;

    @RequestMapping("/config")
    public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            prepare(request, response, model, "indexRequest");
            return "manage/screen/config/index";
        } catch (Throwable e) {
            log.error("ConfigController.indexRequest error", e);
            return TemplateTool.exception(model, TemplateTool.HOME_LAYOUT_DEFAULT_VM, e);
        }
    }

    @RequestMapping("/config/list")
    public String listRequest(HttpServletRequest request, HttpServletResponse response, Model model,
                              String zkId) {
        try {
            prepare(request, response, model, "listRequest");
            model.addAttribute("zkId", zkId);
            return "manage/screen/config/list";
        } catch (Throwable e) {
            log.error("ConfigController.listRequest error", e);
            return TemplateTool.exception(model, TemplateTool.DEFAULT_VM, e);
        }
    }
}
