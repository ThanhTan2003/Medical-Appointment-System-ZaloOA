package com.programmingtechie.zalo_oa_service.video;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.programmingtechie.zalo_oa_service.oa.APIException;
import com.programmingtechie.zalo_oa_service.oa.ZaloOaClient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hien
 */
public class CreateVideoArticle {

    public static void main(String[] args) throws APIException {
        ZaloOaClient client = new ZaloOaClient();
        String access_token = "your_access_token";

        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", access_token);

        JsonObject videoArticle = new JsonObject();
        videoArticle.addProperty("type", "video");
        videoArticle.addProperty("title", "article test");
        videoArticle.addProperty("description", "description");
        videoArticle.addProperty("video_id", "video_id");
        videoArticle.addProperty("avatar", "https://developers.zalo.me/web/static/zalo.png");
        videoArticle.addProperty("comment", "show");

        JsonObject excuteRequest = client.excuteRequest(
                "https://openapi.zalo.me/v2.0/article/create", "POST", null, videoArticle, headers, null);

        System.err.println(excuteRequest);

        System.exit(0);
    }
}
