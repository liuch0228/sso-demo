package com.icoding.controller;

import com.icoding.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/tmall")
    public String index(Model model) {
        model.addAttribute("logoutURL", SSOClientUtil.getServerLogOutUrl());
        return "tmall";
    }

    @RequestMapping("/logOut")
    public void logOut(HttpSession session) {
        session.invalidate();
    }




}
