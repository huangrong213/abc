package com.testcase.http;

import com.tools.excelread.ExcelUtil;
import com.tools.excelread.GetTestCaseExcel;
import com.tools.httprequest.GetHttp;
import io.restassured.response.Response;
import jxl.read.biff.BiffException;
import org.apache.poi.ss.usermodel.Row;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.tools.excelread.HttpJmeterExcelData;
import com.tools.excelread.HRExcelRead;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 李振7
 * Created Time: 2018/1/22 下午3:58
 */

public class testExampleForJmeterData {
    String filePath = "E:/";  //文件路径src/testExampleForJmeterData/GetTestCaseExcel/dubbo
    String fileName = "testcase"; //文件名，不包含文件后缀.xls
    String caseName = "testcase"; //sheet名
    public HttpJmeterExcelData httpJmeterExcelDatademo;

    public testExampleForJmeterData() throws IOException, BiffException {
//        httpJmeterExcelDatademo = new HttpJmeterExcelData(filePath, fileName, caseName);
        //  ArrayList<ArrayList<Row>> list= ExcelUtil.readExcels("E://testcase1.xls");
//        int cellnum= list.get(0).get(0).getLastCellNum();
//        int rownum=list.get(0).get(0).getSheet().getLastRowNum();
//        for (   int i=0;i<rownum;i++)
//        {
////            for (   int j=0;j<cellnum;j++)
////            {
//               String aa=  list.get(0).get(0).getSheet().getRow(i).getCell(0).getStringCellValue();
//                String bb=  list.get(0).get(0).getSheet().getRow(i).getCell(1).getStringCellValue();
//            String cc="123";
//            //}
//
//        }


        //执行用例1

    }


    @DataProvider
    public Object[][] Numbers() throws BiffException, IOException {
        GetTestCaseExcel e = new GetTestCaseExcel(filePath, fileName, caseName);
        return e.getExcelData();
    }

    @Test(dataProvider = "Numbers")
    public void test(HashMap<String, String> data) throws IOException, BiffException {
        // httpJmeterExcelDatademo.test(data);
        //请求类型
//        String protocol = data.get("Protocol");
//        //获取域名(地址)
//        String caseTestDomain = data.get("Domain");
//        //获取路径
//        String casepath = data.get("Path");
//        //获取参数
//        String RequstData = data.get("RequstData");
//        //获取响应码
//        String HttpStatus = data.get("HttpStatus");
//
//        String url = protocol + "://" + caseTestDomain + casepath;
//
//        Response response = GetHttp.getHttp(url, RequstData);
//        //打印响应体
//        response.print();
//        //断言状态码是否为200
//        Assert.assertEquals(HttpStatus, response.getBody().jsonPath().get("code").toString());

    }


}
