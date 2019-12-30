package com.coding.http;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

// https://way.jd.com/jisuapi/query4?shouji=13675165932&appkey=ff839eb8922fa08e096d7b2f9411085b
public class HttpTest {

    public static void main(String[] args) throws Exception {
       /* //定义需要访问的地址 参考https://wx.jdcloud.com/market/api/10348?apiId=10348#
        URL url = new URL("http://way.jd.com/jisuapi/query4");
        //2.连接url
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        // 3.设置请求方式
        connection.setRequestMethod("POST");
        // 4.设置请求参数
        // use the URL connection for output
        connection.setDoOutput(true);
        StringBuilder params = new StringBuilder();
        params.append("shouji=").append("18262611180")
                .append("&appkey=").append("ff839eb8922fa08e096d7b2f9411085b");
        connection.getOutputStream().write(params.toString().getBytes("UTF-8"));

        // 5.发起请求
        connection.connect();

        //6. 接收响应
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
*/
        Map params = new HashMap<String, String>();
        params.put("shouji", "18262611180");
        params.put("appkey", "ff839eb8922fa08e096d7b2f9411085b");
        String response = HttpUtil.sendHttpRequest("http://way.jd.com/jisuapi/query4", params);
        System.out.println(response);
        //{"code":"10000","charge":false,"msg":"查询成功","result":{"status":0,"msg":"ok","result":{"shouji":"13675165932","province":"江苏","city":"南京","company":"中国移动","cardtype":"GSM","areacode":"025"}}}

    }
}
