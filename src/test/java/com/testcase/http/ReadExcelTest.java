package com.testcase.http;

import com.tools.excelread.HRExcelDataRequest;
import com.tools.excelread.HRExcelRead;
import com.tools.excelread.PoiUtil;
import com.tools.sqlrequest.DBHelper;
import io.restassured.response.Response;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class ReadExcelTest {
    String host = "https://tgrasperapi.distrii.com";
    String filePath = "E:\\ApiTest01.xls";
    String sheetName = "sheet1";
    public HRExcelDataRequest HRQ;
    private static int number = 1;

    @BeforeTest
    public void init() {
        //设置需要读取excel的地址，sheet名
        HRQ = new HRExcelDataRequest(filePath, sheetName);
        // Response response = HRExcelDataRequest.ExcelDataRequest();
    }

    @DataProvider
    public Object[][] ExcelData() {
        //获取读取Excel返回的数据
        Object[][] objects = HRExcelRead.readExcel(filePath, sheetName);
        return objects;
    }

    @Test(dataProvider = "ExcelData")
    public void test01(HashMap<String, String> data) throws IOException {
        //获取请求接口后返回的数据
        Response response = HRQ.ExcelDataRequest(data);
        //获取响应结果中的响应状态码
        String code = response.jsonPath().get("code").toString();
        //获取excel用例中的CaseName列名
        String CaseName = data.get("CaseName");
        //获取响应结果中的响应数据
        String dataValue;
        if (response.jsonPath().get("data") == null) {
            dataValue = "null";
        } else {
            dataValue = response.jsonPath().get("data").toString();
        }

        //获取响应结果中的响应信息
        String msg = response.jsonPath().get("msg").toString();
        //断言预期状态码和响应状态码是否一致
        Assert.assertEquals("200", code);
        //打印日志到测试报告中
        Reporter.log("测试用例为：" + CaseName);
        Reporter.log("响应状态码为：" + code);

        //用例运行状态改为Y,(已测试)
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 1, number, "Y");//msg
        //写回响应数据到excel中
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 7, number, dataValue);//data
        //写回响应信息到excel中
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 8, number, msg);//msg
        //设置单元格颜色
        //HRExcelRead.setCellBackgroundColor(filePath, sheetName, 9, number, 0);


        number++;
        //System.out.println(code);
        //Reporter.log("日志测试");
    }


    //读取mysql操作
    public void test02() {
        String DBurl = "jdbc:mysql://127.0.0.1/test23?serverTimezone=UTC&useSSL=false";
        String JdbcName = "com.mysql.cj.jdbc.Driver";
        String UserName = "root";
        String PassWord = "123456";
        String sql = "select * from testuser";
        DBHelper db1 = new DBHelper(DBurl, JdbcName, UserName, PassWord, sql);
    }


}
