package com.course.utils;

import java.io.IOException;
import java.util.*;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONObject;
import org.apache.http.Consts;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


@Log4j
public class HttpClientUtil {
    ProUtil proUtil = new ProUtil();
    private String result;
    private int statusCode;
    private HttpGet get;
    private HttpPost post;
    private CloseableHttpResponse response;
    public static CookieStore cookieStore = new BasicCookieStore();
    private CloseableHttpClient httpCilent = HttpClients.custom().setDefaultCookieStore(cookieStore)
            .setConnectionManagerShared(true).build();
    private RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setSocketTimeout(5000).setRedirectsEnabled(true)
            .build();


    /**
     * 封装get请求
     * @param url
     * @param
     * @return
     */
    public String httpGet(String url,String params,String assertValue) {
        try {
            if (params!=null){
                get = new HttpGet(url+"?"+params);
            }else {
                get = new HttpGet(url);
            }
            get.setConfig(requestConfig);
            get.addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            get.addHeader("Content-Type","application/x-www-form-urlencoded");
            get.addHeader("X-SAAS-Token", proUtil.readProperties("token.properties","X-SAAS-Token"));
            response = httpCilent.execute(get);
            statusCode=response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            System.out.println(result);
            if (statusCode == 200 || statusCode == 302) {
                log.info(url+"接口返回的响应信息为--->"+result);
                assert result.contains(assertValue);
                return result;
            } else {
                log.error(url+"接口请求异常请排查!!!--->"+"错误码为："+statusCode);
                return null;
            }

        }catch (IOException e){
            log.error(e.getMessage());
        }finally {
            if (null != response) {
                try {
                    response.close();
                    httpCilent.close();
                } catch (IOException e) {
                    log.error("释放连接出错");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * post请求FORM参数
     * @param url
     * @param params
     * @return
     */
    public String httpPostForm(String url, String params,String assertValue) {
        post = new HttpPost(url);
        post.setConfig(requestConfig);
        //添加请求头信息
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        post.addHeader("X-SAAS-Token", proUtil.readProperties("token.properties","X-SAAS-Token"));
        List<BasicNameValuePair> list = new ArrayList<>();
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap();
        map = gson.fromJson(params, map.getClass());
        for (Iterator iter = map.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String value = String.valueOf(map.get(name));
            list.add(new BasicNameValuePair(name, value));
        }
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
            post.setEntity(entity);
            response = httpCilent.execute(post);
            statusCode=response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity());
            if (statusCode == 200 || statusCode == 302){
                log.info(url+"接口返回的响应信息为--->"+result);
                assert result.contains(assertValue);
                return result;
            }else{
                log.error(url+"接口请求异常请排查!!!--->"+"错误码为："+statusCode);
                return null;
            }
        }catch (IOException e){
            log.error(url+"接口请求失败--->"+e.getMessage());
        }finally {
            if (null != response) {
                try {
                    response.close();
                    httpCilent.close();
                } catch (IOException e) {
                    log.error("释放连接出错");
                    e.printStackTrace();
                }
            }
        }
            return null;
    }

    /**
     * post请求JSON参数
     * @param url
     * @param params
     */
    public String httpPostJson(String url,String params,String assertValue){
        post = new HttpPost(url);
        post.setConfig(requestConfig);
        //添加请求头信息
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        post.setHeader("content-type", "application/json");
        post.addHeader("X-Source-Platform","iot-mars");
        JSONObject json = JSONObject.fromObject(params);
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        post.setEntity(entity);
        try {
            response = httpCilent.execute(post);
            statusCode = response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity());
            if (statusCode == 200 || statusCode == 302){
                log.info(url+"接口返回的响应信息为--->"+result);
                assert result.contains(assertValue);
                return result;
            }else{
                log.error(url+"接口请求异常请排查!!!--->"+"错误码为："+statusCode);
                return null;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }finally {
            if (null != response) {
                try {
                    response.close();
                    httpCilent.close();
                } catch (IOException e) {
                    log.error("释放连接出错");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 设置cookie
     * @param cookielist
     */
    public static void setCookieStore(List<BasicClientCookie> cookielist ) {
        for(BasicClientCookie cookie:cookielist){
            HttpClientUtil.cookieStore.addCookie(cookie);
        }
    }


    /**
     * 创建cookie
     * @param cookielist
     */
    public static void createCookie(List<BasicClientCookie> cookielist ) {
        for(BasicClientCookie cookie:cookielist){
            HttpClientUtil.cookieStore.addCookie(cookie);
        }
    }

}

