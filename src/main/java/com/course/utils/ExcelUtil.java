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
    private int excelCountRows;
    private String devHost;
    private String realHost;
    private String localHost;

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
     * 获取excel表实际总行数
     * @return
     */
    public int getExcelCountRows() {
        return excelCountRows;
    }

    /**
     * 获取dev环境域名
     * @return
     */
    public String getDevHost() {
        return devHost;
    }

    /**
     * 获取生成环境域名
     * @return
     */
    public String getRealHost() {
        return realHost;
    }

    /**
     * 获取本地环境域名
     * @return
     */
    public String getLocalHost() {
        return localHost;
    }

    /**
     * 获取excel表接口信息
     * @throws IOException
     * @throws BiffException
     */
    public void readApiInfo(String excelPath) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(excelPath));
        Sheet sheet = workbook.getSheet(0);
        String cellInfo[][]  = new String[sheet.getRows()][sheet.getColumns()];
        for (int i = 1; i < sheet.getRows(); i++) {
            for (int j = 0; j < sheet.getColumns(); j++) {
                Cell cell = sheet.getCell(j, i);
                cellInfo[i][j] = cell.getContents();
            }
            this.caseId=cellInfo[i][0];
            this.apiAddress=cellInfo[i][1];
            this.requestMethod=cellInfo[i][2];
            this.requestType=cellInfo[i][3];
            this.param=cellInfo[i][4];
            this.expected=cellInfo[i][5];
            this.headers=cellInfo[i][6];
            this.caseName=cellInfo[i][7];
            this.excelCountRows = i;
            if (this.apiAddress.length()==0){
                log.error("第"+i+"行接口地址为空跳出读取apiInfo数据循环体");
                break;
            }

        }
        workbook.close();
    }

    /**
     * 获取接口访问域名
     * @param excelPath
     * @param RowsNum
     * @throws IOException
     * @throws BiffException
     */
    public void readHost(String excelPath,int RowsNum) throws IOException, BiffException {
        Workbook workbook = Workbook.getWorkbook(new File(excelPath));
        Sheet sheet = workbook.getSheet(1);
        String cellInfo[][] = new String[sheet.getRows()][sheet.getColumns()];
        for (int i = 1; i < sheet.getRows(); i++) {
            for (int j = 0; j < sheet.getColumns(); j++) {
                Cell cell = sheet.getCell(j, i);
                cellInfo[i][j] = cell.getContents();
            }
            this.devHost=cellInfo[RowsNum][1];
            this.realHost=cellInfo[RowsNum][2];
            this.localHost=cellInfo[RowsNum][3];
            if (this.devHost.length()==0||this.realHost.length()==0){
                log.error("第"+i+"行接口地址为空跳出读取host数据循环体");
                break;
            }
        }
    }

}