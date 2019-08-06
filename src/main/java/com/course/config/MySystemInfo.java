package com.course.config;

import com.vimalselvam.testng.SystemInfo;
import org.testng.collections.Maps;

import java.util.Map;

/**
 *测试报告面板样式
 * @author fuyang
 * @date 2019/07/31
 */
public class MySystemInfo implements SystemInfo {
    @Override
    public Map<String, String> getSystemInfo() {
        Map<String,String> systemInfo = Maps.newHashMap();
        systemInfo.put("项目名称","IOT");
        systemInfo.put("测试执行人","芙杨");
        systemInfo.put("测试联系人","芙杨／落叶");
        return systemInfo;
    }

}
