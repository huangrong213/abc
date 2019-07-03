package com.tools.httprequest;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostHttp {

    /**
     * post请求
     *
     * @param url        请求地址
     * @param bodystring 提交的数据
     */
    public static Response postHttp(String url, String bodystring) {
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .body(bodystring)
                .post(url);
        return response;
    }

//    public static Response postWithCookies(String url, String bodystring, String cookies, String headers) {
//        Response response = given()
////                .headers("content-type", "application/json")
////                .cookies("usercookies", "11111111")
////                .body(bodystring)
//                .post("127.0.0.1:1122/postDemo");
//        return response;

//    }


}
