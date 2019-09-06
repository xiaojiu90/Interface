package com.course.controller;

import com.course.model.*;
import com.course.utils.*;
import jxl.read.biff.BiffException;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.testng.annotations.*;
import java.io.IOException;
import java.util.*;

@Log4j
public class SuiteTestCaseCenter {
    ExcelUtil excelUtil = new ExcelUtil();
    HttpClientUtil httpClientUtil = new HttpClientUtil();
    ProUtil proUtil = new ProUtil();
    private String RequestHost;

    /**
     * 获取接口访问域名
     * @throws IOException
     */
    @BeforeClass
    public void getRequestHost() throws IOException, BiffException {
        excelUtil.readHost("TestCase.xls",1);
        this.RequestHost = excelUtil.getDevHost();
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
     * 登陆
     * @throws Exception
     */
    @Test
    public void login() throws Exception {
        excelUtil.readApiInfo("TestCase.xls");
        if (excelUtil.getApiAddress()!=null){
            String result = httpClientUtil.httpGet(RequestHost+excelUtil.getApiAddress(),excelUtil.getParam(),excelUtil.getExpected());
            Thread.sleep(3000);
        }else {
            log.error("登陆账号或密码不能为空！！！");
        }
    }

    /**
     * 遍历请求数据库接口
     * @throws Exception
     */
//    @Test(dataProvider="iotDataProvider")
    @Test
    public void testIotApi() throws Exception {
        for (int i=1;i<=excelUtil.getExcelCountRows();i++){
            excelUtil.readApiInfo("TestCase.xls");
            log.info(excelUtil.getApiAddress());
            if (excelUtil.getApiAddress()!=null && excelUtil.getRequestMethod()!=null){
                if (excelUtil.getRequestMethod().equals("GET")){
                    httpClientUtil.httpGet(RequestHost+excelUtil.getApiAddress(),excelUtil.getParam(),excelUtil.getExpected());
                }
                if (excelUtil.getRequestMethod().equals("POST")){
                    if (excelUtil.getRequestType().equals("FORM")){
                        httpClientUtil.httpPostForm(RequestHost+excelUtil.getApiAddress(),excelUtil.getParam(),excelUtil.getExpected());
                    }else if (excelUtil.getRequestType().equals("JSON")){
                        httpClientUtil.httpPostJson(RequestHost+excelUtil.getApiAddress(),excelUtil.getParam(),excelUtil.getExpected());
                    }else{
                        log.error(excelUtil.getCaseName()+"接口数据库定义的参数请求类型错误！！！");
                        break;
                    }
                }
            }else {
                log.error(excelUtil.getApiAddress()+"的接口数据错误，跳出循环体！！！");
                break;
            }
            Thread.sleep(1000);
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

    public static void main(String[] args) throws Exception {
        SuiteTestCaseCenter as = new SuiteTestCaseCenter();
        as.getRequestHost();
        as.testIotApi();
    }

}
