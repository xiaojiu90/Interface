package com.course.utils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.extern.log4j.Log4j;

import java.io.*;

/**
 * 读取excel表数据
 * @author 芙杨
 * @date 2019/08/20
 */
@Log4j
public class ExcelUtil {
    private String caseId;
    private String apiAddress;
    private String requestMethod;
    private String requestType;
    private String param;
    private String expected;
    private String headers;
    private String caseName;

    /**
     * 获取用例ID
     * @return
     */
    public String getCaseId() {
        return caseId;
    }

    /**
     *获取api地址
     * @return
     */
    public String getApiAddress() {
        return apiAddress;
    }

    /**
     * 获取接口请求方法
     * @return
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * 获取参数类型
     * @return
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * 获取接口参数
     * @return
     */
    public String getParam() {
        return param;
    }

    /**
     * 获取接口响应预期值
     * @return
     */
    public String getExpected() {
        return expected;
    }

    /**
     * 获取请求头信息
     * @return
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * 获取用例名称
     * @return
     */
    public String getCaseName() {
        return caseName;
    }

    /**
     * 获取excel表每个单元格的值
     * @throws IOException
     * @throws BiffException
     */
    public void readExcel(String excelPath,int indexNum) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(excelPath));
        Sheet sheet = workbook.getSheet(0);
        String cellInfo[][]  = new String[sheet.getRows()][sheet.getColumns()];
        for (int i = 1; i < sheet.getRows(); i++) {
            for (int j = 0; j < sheet.getColumns(); j++) {
                Cell cell = sheet.getCell(j, i);
                cellInfo[i][j] = cell.getContents();
            }
            this.caseId=cellInfo[indexNum][0];
            this.apiAddress=cellInfo[indexNum][1];
            this.requestMethod=cellInfo[indexNum][2];
            this.requestType=cellInfo[indexNum][3];
            this.param=cellInfo[indexNum][4];
            this.expected=cellInfo[indexNum][5];
            this.headers=cellInfo[indexNum][6];
            this.caseName=cellInfo[indexNum][7];
            if (this.apiAddress.length()==0){
                log.error("第"+i+"行接口地址为空退出读取excel数据循环体");
                break;
            }
        }
        workbook.close();
    }

    public static void main(String[] args) throws IOException, BiffException {
        ExcelUtil as = new ExcelUtil();
        as.readExcel("/Users/zl/Documents/workspace/IotInterface/src/main/resources/TestCase.xls",3);
    }

}
