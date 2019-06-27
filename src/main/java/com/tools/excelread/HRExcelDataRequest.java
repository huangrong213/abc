package com.tools.excelread;

import com.tools.httprequest.GetHttp;
import com.tools.httprequest.PostHttp;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class HRExcelDataRequest {

    //Excel路径
    public String filePath;
    //sheet名
    public String sheetName;

    public HRExcelDataRequest(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

//    @DataProvider
//    public Object[][] ExcelData() {
//        //获取读取Excel返回的数据
//        Object[][] objects = HRExcelRead.readExcel(filePath, sheetName);
//        return objects;
//    }

    /**
     * 根据用例里的数据发送get/post请求
     *
     * @param data
     */
    //@Test(dataProvider = "ExcelData")
    public static Response ExcelDataRequest(HashMap<String, String> data) {
        String CaseID = data.get("CaseID");//用例ID
        String URL = data.get("URL");//接口地址
        String Methond = data.get("Methond");//请求方式get/post
        String Parames = data.get("Parames");//请求参数
        String code = data.get("HttpCode");//响应状态码
        Response response = null;
        if (Methond.equalsIgnoreCase("get")) {
            //请求方式是get
            response = GetHttp.getHttp(URL, Parames);
            //打印响应体
            response.print();
        } else if (Methond.equalsIgnoreCase("post")) {
            //请求方式是post

            response = PostHttp.postHttp(URL, Parames);
            //打印响应体
            response.print();
        }
        //执行完请求后将结果写回数据到excel

//        HRExcelRead.writeInfoToExcelByCell(filePath, 6, number, data1);//data
//        HRExcelRead.writeInfoToExcelByCell(filePath, 7, number, msg);//msg

        return response;

    }


}
