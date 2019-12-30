package com.icoding.pojo;


/**
 * 前端传递用户登出地址和session到sso认证中心，认证中心保存到数据库中，以便登出时使用
 */

public class ClientInfoVO {
    /**
     * 用户登出地址
     */
    private String clientUrl;
    /**
     * 用户sessionId,每个用户sessionId不同
     */
    private String jsessionId;


    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getJsessionId() {
        return jsessionId;
    }

    public void setJsessionId(String jsessionId) {
        this.jsessionId = jsessionId;
    }
}
