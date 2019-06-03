package com.example.server.manager.web.controller.manage.urgentconfig;

import com.example.server.manager.web.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class UrgentconfigController extends BaseController {

    @RequestMapping("/urgentconfig")
    public String indexRequest(HttpServletRequest request, HttpServletResponse response, Model model) {
        prepare(request, response, model, "indexRequest");
        return "manage/screen/urgentconfig/index";
    }
}
