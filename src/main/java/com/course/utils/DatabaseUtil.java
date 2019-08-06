package com.course.utils;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * 返回一个sql执行器
 */
public class DatabaseUtil {
    public static SqlSession getSqlSession() throws IOException {
        //获取数据库配置的资源文件
        Reader reader = Resources.getResourceAsReader("databaseConfig.xml");

        //加载配置文件
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);

        //sqlSession执行配置文件中的sql语句
        SqlSession session = factory.openSession();

        return session;
    }

}
