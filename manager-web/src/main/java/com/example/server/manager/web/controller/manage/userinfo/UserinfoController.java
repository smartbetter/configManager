package com.example.server.manager.web.controller.manage.userinfo;

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
public class UserinfoController extends BaseController {

    @RequestMapping("/userinfo")
    public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            prepare(request, response, model, "indexRequest");
            if (currentUser == null) {
                return TemplateTool.error(model, TemplateTool.HOME_LAYOUT_DEFAULT_VM,"currentUser is null");
            }
            model.addAttribute("user", currentUser);
            return "manage/screen/userinfo/index";
        } catch (Throwable e) {
            log.error("UserinfoController.indexRequest error", e);
            return TemplateTool.exception(model, TemplateTool.HOME_LAYOUT_DEFAULT_VM, e);
        }
    }
}
