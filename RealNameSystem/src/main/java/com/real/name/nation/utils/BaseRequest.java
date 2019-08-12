package com.real.name.nation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jim.z.hu on 2018/7/11.
 */
public abstract class BaseRequest {
    /**
     * 处理response
     */
    public void processResponse(CloseableHttpResponse response) {
    }

    /**
     * post请求数据
     *
     * @param url            URL
     * @param requestHeaders 请求头
     * @param dataMap        请求参数
     */
    public String postData(String url, Map<String, String> requestHeaders, Map<String, String> dataMap) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        if (dataMap != null) {
            dataMap.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
        }
        if (requestHeaders != null) {
            requestHeaders.forEach(httpPost::setHeader);
        }
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");
            urlEncodedFormEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            processResponse(response);
            return getResponseContent(response);
        } catch (IOException e) {
            return "";
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取response内容
     */
    private String getResponseContent(CloseableHttpResponse response) {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            return "";
        }
    }

}