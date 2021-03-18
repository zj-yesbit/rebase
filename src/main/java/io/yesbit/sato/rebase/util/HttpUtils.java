package io.yesbit.sato.rebase.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;


@Component
public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    @Autowired
    RestTemplate restTemplate;

    public HttpHeaders getHttpJsonHeaders() {
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        return headers;
    }

    public HttpHeaders getHttpJsonNotUTFHeaders() {
        MediaType type = MediaType.parseMediaType("application/json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        return headers;
    }

    public HttpHeaders getHttpTextHeaders() {
        MediaType type = MediaType.parseMediaType("text/plain; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        return headers;
    }

    public HttpHeaders getHttpAuthJsonHeaders(String token) {
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        headers.add("Authorization",  token);
        return headers;
    }

    public HttpHeaders getHttpAuthTextHeaders(String token) {
        MediaType type = MediaType.parseMediaType("text/plain; charset=UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(type);
        headers.add("Authorization", "Basic " + token);
        return headers;
    }

    public HttpHeaders getHttpJsonHeaders(Map<String, String> headerMap) {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, String> h : headerMap.entrySet()) {
            if (h.getKey().equalsIgnoreCase("content-type")) {
                headers.setContentType(MediaType.parseMediaType("text/plain; charset=UTF-8"));
            } else {
                headers.add(h.getKey(), h.getValue());
            }
        }
        return headers;
    }

    public String httpPostForJSON(String url, String httpEntity) {
        LOGGER.debug("request url:{},body:{}", url, httpEntity);
        HttpEntity<String> formEntity = new HttpEntity<>(httpEntity, getHttpJsonHeaders());
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);
            String body = responseEntity.getBody();
            LOGGER.debug("response body:{}", body);
            return body;
        } catch (Exception e) {
            LOGGER.error("httpPostForJSON通信时异常", e);
            return null;
        }

    }

    public  Map<Object,Object>  httpExchangeForGet(String url, Map<String,Object> param,String token) {
        LOGGER.debug("request url:{},body:{}", url, param);
        HttpHeaders headers = getHttpAuthJsonHeaders(token);
        try {
            if(param==null) {
                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<String>(headers),
                        Map.class);
                Map<Object, Object> body = response.getBody();
                LOGGER.debug("response body:{}", body);
                return body;
            }else{
                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        new HttpEntity<String>(headers),
                        Map.class,
                        param);
                Map<Object, Object> body = response.getBody();
                LOGGER.debug("response body:{}", body);
                return body;
            }

        } catch (Exception e) {
            LOGGER.error("httpPostForJSON通信时异常", e);
            return null;
        }
    }
        public  Map<Object,Object>  httpExchangeForPost(String url, String postEntity, String token) {
            LOGGER.debug("request url:{},body:{}", url, postEntity);
            try {
                MediaType type = MediaType.parseMediaType("application/json");
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(type);
                headers.add("Accept", MediaType.ALL_VALUE);
                headers.add("Authorization", token);
                HttpEntity<String> formEntity = new HttpEntity<String>(postEntity, headers);
                ResponseEntity<Map> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        formEntity,
                        Map.class);
                    Map<Object, Object> body = response.getBody();
                    LOGGER.debug("response body:{}", body);
                    return body;
            } catch (Exception e) {
                LOGGER.error("httpPostForJSON通信时异常", e);
                return null;
            }
    }




    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public JSONObject sendPost(String url, JSONObject param) {
        StringBuilder sb = new StringBuilder();
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return JSON.parseObject(sb.toString());
    }


    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */

    public String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            //connection.setRequestProperty("Content-Type", "application/json");
            //  connection.setRequestProperty("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ6aGlqaWFAeWVzYml0LmNhIiwiZXhwIjoxNjE1NzY4Mzc2fQ._uZ5yIynLGD-ClHHy0F5hHqgx7snizecpF5bB0BMUBw0IvpL6Y55VJ-NPYux7SfHi5opqtG51zMrwRSUCHTRgQ");
           // connection.setRequestProperty("connection", "Keep-Alive");
           // connection.setRequestProperty("user-agent",
           //         "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;

        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


}
