package com.course.utils;

import com.course.model.IotApi;
import com.course.model.IotHost;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import java.io.IOException;

@Log4j
public class GetApi {
    private String apiAddress;
    private String requestParam;
    private String requestMethod;
    private String paramType;
    private String assertValue;
    private String caseName;
    private String devHost;
    private String realHost;
    private String localHost;

    //获取api地址
    public String getApiAddress() {
        return this.apiAddress;
    }

    //获取dev接口访问域名
    public String getDevtHost() {
        return this.devHost;
    }

    //获取线上接口访问域名
    public String getRealHost() {
        return this.realHost;
    }

    //获取本地接口访问域名
    public String getLocalHost() {
        return localHost;
    }

    //获取接口请求参数
    public String getRequestParam() {
        return this.requestParam;
    }

    //获取接口请求方法
    public String getRequestMethod() {
        return this.requestMethod;
    }

    //获取接口请求参数类型
    public String getParamType() {
        return this.paramType;
    }

    //获取断言值
    public String getAssertValue() {
        return this.assertValue;
    }

    //获取用例名称
    public String getCaseName() {
        return this.caseName;
    }

    /**
     * 根据id读取api信息
     * @return
     * @throws IOException
     * @param
     */
    public IotApi getApiInfo(int id) throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        IotApi iotApi = session.selectOne("getApi", id);
        if (iotApi != null) {
            log.info("iot_api表中id为" + id + "的查询结果是--->" + iotApi);
            if (iotApi.getApi_address()!= null && iotApi.getApi_address().length() > 0) {
                this.apiAddress = iotApi.getApi_address();
            }
            if (iotApi.getRequest_param() != null && iotApi.getRequest_param().length() > 0) {
                this.requestParam = iotApi.getRequest_param();
            }
            if (iotApi.getRequest_method() !=null && iotApi.getRequest_method().length() > 0){
                this.requestMethod = iotApi.getRequest_method();
            }
            if (iotApi.getParam_type() != null && iotApi.getParam_type().length() > 0) {
                this.paramType = iotApi.getParam_type();
            }
            if (iotApi.getAssert_value() != null && iotApi.getAssert_value().length() > 0) {
                this.assertValue = iotApi.getAssert_value();
            }
            if (iotApi.getCase_name() != null && iotApi.getCase_name().length() > 0) {
                this.caseName = iotApi.getCase_name();
            }

            return iotApi;
        }

        log.error("id为" + id + "的数据在iot_api表中不存在或字段值为空！！！");
        return null;

    }

    /**
     * 根据id获取访问域名
     * @return
     * @throws IOException
     */
    public IotHost getHost(int hostId) throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        IotHost iotHost = session.selectOne("getHost", hostId);
        if (iotHost != null) {
            log.info("iot_host表中hostId为"+hostId+"的查询结果是--->" + iotHost);
            if (iotHost.getDev_host() != null && iotHost.getDev_host().length() > 0) {
                this.devHost = iotHost.getDev_host();
            }
            if (iotHost.getReal_host() != null && iotHost.getReal_host().length() > 0) {
                this.realHost = iotHost.getReal_host();
            }
            if (iotHost.getLocal_host()!=null && iotHost.getLocal_host().length()>0){
                this.localHost = iotHost.getLocal_host();
            }
            return iotHost;
        }
        log.error("id为" + hostId + "的数据在iot_host表中不存在或字段值为空！！！");
        return null;
    }

}
