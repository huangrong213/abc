package com.tools.excelread;

import com.tools.Asserts.Assertion;
import com.tools.Asserts.AssertListener;
import com.tools.httprequest.GetHttp;
import com.tools.httprequest.PostHttp;
import com.tools.log4j.LoggerControler;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tools.log4j.LoggerControler.getLogger;

@Listeners(com.tools.Asserts.AssertListener.class)
public class HRExcelDataRequest {
    final static LoggerControler log = getLogger(HRExcelDataRequest.class);
    //Excel路径
    public static String filePath;
    //sheet名
    public static String sheetName;
    //定义要操作的行，从第1行开始，(第0行是参数名行)
    private static int row = 1;

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
     * @param data Excel表格中的数据
     */
    //@Test(dataProvider = "ExcelData")
    public static Response ExcelDataRequest(HashMap<String, String> data) {
        //以下是获取Excel表格中的字段
        String CaseID = data.get("CaseID");//获取CaseID
//        String Run = data.get("Run");//用例的运行状态，Y为已运行
        String CaseName = data.get("CaseName");//获取CaseName
        String URL = data.get("URL");//获取接口地址
        String Methond = data.get("Methond");//获取请求方式get/post
        String Req_Parames = data.get("Req_Parames");//获取请求参数
        String Check = data.get("Check");//获取检查点
        String Rsp_Data = data.get("Rsp_Data");//获取请求返回数据
        String Rsp_Msg = data.get("Rsp_Msg");//获取请求返回的信息
//        String TestResult = data.get("TestResult");//用例是否执行成功，用颜色标识

        Response response = null;
        log.info("开始执行用例： " + CaseName);
        //判断列表中的请求方式是Get还是Post，不区分大小写
        if (Methond.equalsIgnoreCase("get")) {
            //请求方式是get
            response = GetHttp.getHttp(URL, Req_Parames);
            //打印响应体
//            response.print();
        } else if (Methond.equalsIgnoreCase("post")) {
            //请求方式是post
            response = PostHttp.postHttp(URL, Req_Parames);
            //打印响应体
            response.print();
        }
        //执行完请求后将结果写回数据到excel
//        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 6, row, "");//data
//        HRExcelRead.writeInfoToExcelByCell(filePath, 7, number, msg);//msg
        //断言，写入返回数据
        Asserted(response, data);
        row++;
        return response;

    }


    /**
     * 1、断言方法
     * 2、写回数到excel
     *
     * @param response 接口响应数据
     * @param data     excel用例数据
     */
    public static void Asserted(Response response, HashMap<String, String> data) {
        //获取excel用例中的CaseName列名
        String CaseName = data.get("CaseName");
        //获取响应结果中的响应数据
        String dataValue;
        //如果响应的data内容是null
        if (response.jsonPath().get("data") == null) {
            //设置写入的返回数据就是null
            dataValue = "null";
        } else {
            //写入的数据是实际返回的数据
            dataValue = response.jsonPath().getString("data");
        }
        //获取响应结果中的响应信息
        String msg = response.jsonPath().getString("msg");
        Reporter.log("测试用例为：" + CaseName);
        //获取excel中定义的状态码
        String Check = data.get("Check");
        HashMap<String, String> list = CheckPoint(Check);
        int count = 0;
        //获取检查点，与实际返回数据进行匹配
        for (Map.Entry<String, String> entry : list.entrySet()) {
            //获取excel中的检查点
            String check_excel = entry.getValue();
            //获取与excel检查点对应的返回数据
            String rep_check = response.jsonPath().getString(entry.getKey());
            if (check_excel.equals(rep_check)) {
                count++;
            }
        }
        Assertion.verifyEquals(count, list.size());
        int colorType;
        //判断检查点是否全部通过
        if (count == list.size()) colorType = 1;//全通过单元格设置为1，1为绿色
        else colorType = 0;//2为红色

        //设置单元格颜色
        HRExcelRead.setCellBackgroundColor(filePath, sheetName, 9, row, colorType);
        //用例运行状态改为Y,(Y代码已执行测试)
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 1, row, "Y");//msg
        //将响应data数据写入excel中
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 7, row, dataValue);//data
        //将响应msg信息写入excel中
        HRExcelRead.writeInfoToExcelByCell(filePath, sheetName, 8, row, msg);//msg
        log.info("此条用例执行完毕！响应信息为：" + msg);
    }

    /**
     * 检查点数据转化
     *
     * @param json 要检查的字符串
     * @return
     */
    public static HashMap<String, String> CheckPoint(String json) {
        HashMap<String, String> list = new HashMap<String, String>();
        String[] str = json.split(",");
        for (int i = 0; i < str.length; i++) {
            String[] strr = str[i].split("=");
            list.put(strr[0], strr[1]);
        }
        return list;
    }

}
