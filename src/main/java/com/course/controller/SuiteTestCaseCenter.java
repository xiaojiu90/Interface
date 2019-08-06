package com.course.controller;

import com.course.model.*;
import com.course.utils.*;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.*;

@Log4j
public class SuiteTestCaseCenter {
    GetApi getApi = new GetApi();
    HttpClientUtil httpClientUtil = new HttpClientUtil();
    ProUtil proUtil = new ProUtil();
    private String RequestHost;
    private String LocalHsot;


    /**
     * 获取接口访问域名
     * @throws IOException
     */
    @BeforeClass
    public void getRequestHost() throws IOException {
        getApi.getHost(1);
        this.RequestHost = getApi.getDevtHost();
        this.LocalHsot = getApi.getLocalHost();
    }

    /**
     * 测试完成清理测试数据
     * @throws IOException
     */
    @AfterClass
    public void initialize() throws IOException {
        proUtil.clearData();
    }

    /**
     * iot登陆(获取token)
     * @throws Exception
     */
    @Test
    public void login() throws Exception {
        getApi.getApiInfo(1);
        if (getApi.getRequestParam()!=null){
            String result = httpClientUtil.httpPostJson(RequestHost+getApi.getApiAddress(),getApi.getRequestParam(),getApi.getAssertValue());
//            String tokenValue = proUtil.getOneJson(result,"access_token");
//            proUtil.writeProperties("token.properties","access_token",tokenValue);
//            Thread.sleep(3000);
        }else {
            log.error("登陆账号或密码不能为空！！！");
        }
    }

    /**
     * 遍历请求数据库接口
     * @throws Exception
     */
    @Test(dataProvider="iotDataProvider")
    public void testIotApi(IotApi iotApi) throws Exception {
        for (int id=2;id<=proUtil.getDataBaseRows();id++){
            getApi.getApiInfo(id);
            if (getApi.getRequestMethod()!=null && getApi.getParamType()!=null){
                if (getApi.getRequestMethod().equals("GET")){
                    httpClientUtil.httpGet(RequestHost+getApi.getApiAddress(),getApi.getRequestParam(),getApi.getAssertValue());
                }
                if (getApi.getRequestMethod().equals("POST")){
                    if (getApi.getParamType().equals("FORM")){
                        httpClientUtil.httpPostForm(RequestHost+getApi.getApiAddress(),getApi.getRequestParam(),getApi.getAssertValue());
                    }else if (getApi.getParamType().equals("JSON")){
                        httpClientUtil.httpPostJson(RequestHost+getApi.getApiAddress(),getApi.getRequestParam(),getApi.getAssertValue());
                    }else{
                        log.error(getApi.getCaseName()+"接口数据库定义的参数请求类型错误！！！");
                        break;
                    }
                }
            }else {
                log.error(getApi.getApiAddress()+"的接口请求方式/参数类型为空或错误，跳出循环体！！！");
                break;
            }
            Thread.sleep(2000);
        }
    }

    @DataProvider(name="iotDataProvider")
    private Iterator<Object[]> iotDataProvider() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        List<Object> item = new ArrayList<Object>();
        for (int i=2;i<=proUtil.getDataBaseRows();i++){
            IotApi iotApi = sqlSession.selectOne("getApi",i);
            item.add(iotApi);
        }
        List<Object[]> datas = new ArrayList<Object[]>();
        for (Object u : item) {
            datas.add(new Object[]{u});
        }
        return datas.iterator();

    }

}
