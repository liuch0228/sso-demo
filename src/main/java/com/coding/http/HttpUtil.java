package com.coding.http;

import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class HttpUtil {

    public static String sendHttpRequest(String httpURL, Map<String, String> params) throws Exception {
        URL url = new URL(httpURL);
        //2.连接url
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 3.设置请求方式
        connection.setRequestMethod("POST");
        // 4.设置请求参数
        // use the URL connection for output
        connection.setDoOutput(true);
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        Assert.notEmpty(params, "Request params must not empty");
        StringBuilder sb = new StringBuilder();
       for (Map.Entry<String,String> param : params.entrySet()){
           sb.append("&").append(param.getKey()).append("=").append(param.getValue());
       }
//       sb.toString().substring(1);  // 去除开头多余的&
        connection.getOutputStream().write(sb.toString().substring(1).getBytes("UTF-8"));
        // 5.发起请求
        connection.connect();
        //6. 接收响应
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        return response;
    }


}
