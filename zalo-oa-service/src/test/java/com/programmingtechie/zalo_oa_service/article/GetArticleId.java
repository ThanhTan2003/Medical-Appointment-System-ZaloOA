package com.programmingtechie.zalo_oa_service.article;

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
public class GetArticleId {

    public static void main(String[] args) throws APIException {
        ZaloOaClient client = new ZaloOaClient();
        String access_token = "your_access_token";

        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", access_token);

        JsonObject body = new JsonObject();
        body.addProperty(
                "token", "fvXiHxFn+7lOd2E1LxOxh3cs2cu0KqwwKCQhbSE06b4XwSqinw7F8uwrSPt4fWAkwnloDVzM07tk0KiqvLudSA==");

        JsonObject excuteRequest =
                client.excuteRequest("https://openapi.zalo.me/v2.0/article/verify", "POST", null, body, headers, null);

        System.err.println(excuteRequest);

        System.exit(0);
    }
}
