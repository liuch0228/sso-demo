package com.icoding.db;

import com.icoding.pojo.ClientInfoVO;

import java.util.*;

/**
 * 模拟数据库存储
 */
public class MockDB {

    /**
     * 保存用户登录token
     */
    public static Set<String> T_TOKEN = new HashSet<>();
    /**
     * 保存用户登出信息，一个用户可能同时登录多个系统，map的key就是token,客户端登陆成功时，需要将 ClientInfoVO带给SSO认证中心
     */
    public static Map<String, List<ClientInfoVO>> T_CLIENT_INFO = new HashMap<>();
}
