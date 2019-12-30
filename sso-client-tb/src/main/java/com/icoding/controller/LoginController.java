package com.icoding.controller;

import com.icoding.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    /*@RequestMapping("/taobao")
    public String index(){
        return "taobao";
    }*/

    @RequestMapping("/taobao")
    public String index(Model model){
        // 返回SSO认证中心的登出地址（注销session）给前端页面
        model.addAttribute("logoutURL", SSOClientUtil.getServerLogOutUrl());
        return "taobao";
    }

    @RequestMapping("/logOut")
    public void logOut(HttpSession session) {
        session.invalidate();
    }


}
