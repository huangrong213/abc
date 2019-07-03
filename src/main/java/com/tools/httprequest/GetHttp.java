package com.tools.httprequest;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;


public class GetHttp {


    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 请求参数
     */
    public static Response getHttp(String url, String params) {
        Response response = given()
                .contentType("application/json;charset=UTF-8")
                .param(params)
                .get(url);
        return response;

    }

    /**
     * 检查是否正常
     *
     * @return
     */
    public boolean requsetCheck() {

        return true;

    }


}
