package com.icoding.interceptor;

import com.icoding.utils.HttpUtil;
import com.icoding.utils.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Objects;

public class SsoClientInterceptor implements HandlerInterceptor {


    // true:放行 false 拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.判断是否存在会话
        HttpSession session = request.getSession();
        // isLogin 会话存在标识
        Boolean isLogin = (Boolean)session.getAttribute("isLogin");
        if(null !=isLogin && isLogin){
            return  true;  // 已登录，放行
        }

        // 2. 验证token
        String token = (String)request.getParameter("token");
        if(null!= token && !"".equals(token)){
            System.out.println("检测到服务器token信息"  );
            // 防止token伪造，拿到服务器端去验证
            // 服务器地址
            String httpUrl = SSOClientUtil.SERVER_URL_PREFIX + "/verify";
            // 需要验证的参数
            try {
                HashMap<String, String> params = new HashMap<>();
                // 客户端登陆成功！
                params.put("token", token);
                // 原来只把token携带给SSO认证中心，为了SSO登出，还需要将客户端登出地址clientUrl和sessionId给SSO认证中心存储
                // 客户端登出地址，实际上就是一个logout请求
                params.put("clientUrl", SSOClientUtil.getClientLogOutUrl());
                params.put("jsessionId", session.getId());
                // 请求SSO认证中心验证token 以及登录信息
               String verify =  HttpUtil.sendHttpRequest(httpUrl, params);
               if(Objects.equals("true", verify)){
                   // 服务器端校验token通过
                   System.out.println("服务器端校验token通过");
                   session.setAttribute("isLogin", true);
                   return true;
               }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("验证异常");
            }


        }

        //  没有会话，跳转到统一 认证中心,检测系统是否登录
        // http://www.sso.com:8001/checkLogin?redirectUrl=http://www.tb.com:8002
        System.out.println("没有检测到token，redirect to sso-server");
        SSOClientUtil.redirectToSSOURL(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
