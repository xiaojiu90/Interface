package com.course.utils;

import com.alibaba.fastjson.*;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import java.io.*;
import java.util.*;
import java.util.regex.*;

@Log4j
public class ProUtil{
    Properties properties = new Properties();
    private int countRows;

    /**
     * 获取数据库总行数
     * @return
     * @throws IOException
     */
    public int getDataBaseRows() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        this.countRows = session.selectOne("getLength");
        return  countRows;
    }

    /**
     * 清理数据库测试数据
     * @throws IOException
     */
    public void clearData() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        session.delete("clearTestData");
        session.commit();
    }

    /**
     * 读文件内容
     * @param filePath
     * @param key
     * @return
     */
    public String readProperties(String filePath,String key){
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedInputStream in = new BufferedInputStream(inputStream);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }

    /**
     * 写入文件内容
     * @param filePath
     * @param key
     * @param value
     */
    public void writeProperties(String filePath,String key,String value){
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            properties.setProperty(key,value);
            properties.store(file,key);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * 提取单层json值
     * @param content
     * @param key
     * @return
     */
    public String getOneJson(String content,String key){
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getString(key);
    }


    /**
     * 提取多层json值(value里包含一个json)
     * @param content
     * @param oneLevelKey
     * @param twoLevelKey
     * @return
     */
    public String getMultipleJson(String content,String oneLevelKey,String twoLevelKey){
        JSONObject jsonObject = JSONObject.parseObject(content);
        return jsonObject.getJSONObject(oneLevelKey).getString(twoLevelKey);
    }

    /**
     * 正则匹配出指定的用例名
     * @param source
     * @return
     */
    public  String getContent(String source){
        String regex="case_name=.*";
        Matcher matcher = Pattern.compile(regex).matcher(source);
        String result = null;
        if(matcher.find()){
            result = matcher.group();
        }
        return result;
    }

}
