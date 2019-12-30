package com.icoding.controller;

import com.icoding.db.MockDB;
import com.icoding.pojo.ClientInfoVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
public class SsoServerController {

    @RequestMapping("/index")
    public String index(){
        return "login";
    }

    /**
     *
     * @param username 登录账户
     * @param password 登陆密码
     * @param redirectUrl 登录时的页面地址，也就是登录成功后重定向url（登录页面要用隐藏域携带 ）
     * @param session 服务器session
     * @param model 返回给客户端的数据
     * @return
     */
    @RequestMapping("/login")
    public String login(String username,String password,String redirectUrl, HttpSession session, Model model){

        System.out.println("login");
        // 模拟数据
        if("admin".equals(username) && password.equals("123456")){
            // 1.登录成功，创建一个token,
            String token = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println("gen token succes");
            // 2.保存token到数据库 ，这里模拟一下数据库
            MockDB.T_TOKEN.add(token);
            // 3. 在服务器中存在会话信息，和客户信息没有关系
            session.setAttribute("token", token);
            // 4. 客户端也要保存会话信息，这个很重要！！！！
            model.addAttribute("token", token);
            // 登录成功，返回到来时的地方！
            System.out.println("sso-server: redirectUrl=" + redirectUrl);
            return "redirect:" + redirectUrl; // 从哪来，回哪去 ?token=xxxx   http://www.tb.com:8002/taobao?token=6fd8678740a145c98e39324b820dde9f
        }
        // 登录失败,需要把重定向地址带到登陆页面
        model.addAttribute("redirectUrl", redirectUrl);
        // 登陆失败，返回登陆页面 login.jsp
        return "login";
    }

    /**
     *
     * @param redirectUrl
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/checkLogin")
    public String checkLogin(String redirectUrl, HttpSession session, Model model){
        System.out.println("start check is login");
      // 判断用户是否拥有全局的会话token
        //第一次登录，没有token   // 系统二登录，被重定向到这里来，发现系统一登录时已经产生过token了
        String token = (String)session.getAttribute("token");
        if(StringUtils.isEmpty(token)){
            // 没有全局会话，去登录页面,客户端请求时携带的redirectUrl也需要带上（我从哪里来不能丢掉了，登录页面需要带上这个参数去请求登录接口）
            model.addAttribute("redirectUrl",redirectUrl);
            System.out.println("redirect to login.jsp,redirectUrl=" + redirectUrl);
            return "login";
        } else {
            // 存在全局会话，返回到来的地方（客户端请求时携带的redirectUrl）
            //  回到客户端后，会产生一个cookie， 这个cookie是请求checkLogin的，系统二登录，请求头里面也是这个cookie值
            model.addAttribute("token",token);
            System.out.println("redirect to original page: " + redirectUrl +  "-"  + token);
            return "redirect:" + redirectUrl;
        }
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verifyToken(String token, String clientUrl, String jsessionId){
        if(MockDB.T_TOKEN.contains(token)){
            System.out.println("服务器端校验token通过");
            // 服务端验证通过，需要将客户端请求携带的sessionId和登出地址保存
            List<ClientInfoVO> clientInfoList = MockDB.T_CLIENT_INFO.get(token);
            if(null == clientInfoList){
                clientInfoList = new ArrayList<>();
                MockDB.T_CLIENT_INFO.put(token, clientInfoList);
            }
            ClientInfoVO clientInfo = new ClientInfoVO();
            clientInfo.setClientUrl(clientUrl);
            clientInfo.setJsessionId(jsessionId);
            clientInfoList.add(clientInfo);

            return "true";
        }
        return "false";
    }

    /**
     *  注销请求，需要注意的是，这里是手动点击注销按钮才会销毁session!
     *  而session过期后，通知子系统注销会话的场景，这里不适用！！！
     * @param session
     * @return
     */
    @RequestMapping("/logOut")
    public String logout(HttpSession session){
        session.invalidate();
        // 通知淘宝和天猫销毁session
        // 会话注销后，回到登录页面
        return "login";

    }



}
