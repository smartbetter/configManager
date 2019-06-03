package com.example.server.manager.web.controller.home;

import com.example.server.manager.common.util.TemplateTool;
import com.example.server.manager.web.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class IndexController extends BaseController {

    @RequestMapping("/")
    public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            prepare(request, response, model, "indexRequest");
            return "home/screen/index";
        } catch (Throwable e) {
            log.error("IndexController.indexRequest error", e);
            return TemplateTool.exception(model, TemplateTool.HOME_LAYOUT_DEFAULT_VM, e);
        }
    }
}
