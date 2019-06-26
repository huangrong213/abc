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

import java.util.HashMap;

public class ReadExcelTest {
    String host = "https://tgrasperapi.distrii.com";
    String filePath = "E:\\aiptest.xlsx";
    String sheetName = "sheet1";
    public HRExcelDataRequest HRQ;

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
    public void test01(HashMap<String, String> data) {

        Response response = HRQ.ExcelDataRequest(data);
        String code = response.jsonPath().get("code").toString();
        Assert.assertEquals("200", code);
        Reporter.log("响应状态码为：" + code);
        //System.out.println(code);
        //Reporter.log("日志测试");

    }

    @Test
    public void test02() {
        String DBurl = "jdbc:mysql://127.0.0.1/test23?serverTimezone=UTC&useSSL=false";
        String JdbcName = "com.mysql.cj.jdbc.Driver";
        String UserName = "root";
        String PassWord = "123456";
        String sql = "select * from testuser";
        DBHelper db1 = new DBHelper(DBurl, JdbcName, UserName, PassWord, sql);
    }


}
