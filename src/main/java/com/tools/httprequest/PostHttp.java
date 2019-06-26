package com.tools.httprequest;

import com.tools.log4j.LoggerControler;
import io.restassured.response.Response;

import static com.tools.log4j.LoggerControler.getLogg;
import static io.restassured.RestAssured.given;

public class PostHttp {
    final static LoggerControler log = getLogg(GetHttp.class);

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


}
