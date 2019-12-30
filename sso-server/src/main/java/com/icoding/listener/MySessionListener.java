package com.icoding.listener;

import com.icoding.db.MockDB;
import com.icoding.pojo.ClientInfoVO;
import com.icoding.utils.HttpUtil;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

public class MySessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    /**
     * 销毁事件
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String token = (String) session.getAttribute("token");
        // 销毁用户信息
        // 1. 数据库清除用户token
        MockDB.T_TOKEN.remove(token);
        List<ClientInfoVO> clientInfoList = MockDB.T_CLIENT_INFO.remove(token);
        // 2. 遍历通知客户端注销
        for (ClientInfoVO clientInfo: clientInfoList){
            try {
                HttpUtil.sendHttpRequest(clientInfo.getClientUrl(), clientInfo.getJsessionId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
