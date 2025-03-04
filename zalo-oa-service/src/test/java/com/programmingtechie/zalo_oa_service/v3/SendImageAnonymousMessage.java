package com.programmingtechie.zalo_oa_service.v3;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
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
public class SendImageAnonymousMessage {

    public static void main(String[] args) throws APIException {
        ZaloOaClient client = new ZaloOaClient();
        String access_token = "your_access_token";

        Map<String, String> headers = new HashMap<>();
        headers.put("access_token", access_token);

        JsonObject element = new JsonObject();
        element.addProperty("media_type", "image");
        element.addProperty("url", "https://stc-developers.zdn.vn/images/bg_1.jpg");

        JsonArray elements = new JsonArray();
        elements.add(element);

        JsonObject payload = new JsonObject();
        payload.add("elements", elements);
        payload.addProperty("template_type", "media");

        JsonObject attachment = new JsonObject();
        attachment.addProperty("type", "template");
        attachment.add("payload", payload);

        JsonObject message = new JsonObject();
        message.add("attachment", attachment);
        message.addProperty("text", "text_message");

        JsonObject recipient = new JsonObject();
        recipient.addProperty("conversation_id", "conversation_id");
        recipient.addProperty("anonymous_id", "anonymous_id");

        JsonObject body = new JsonObject();
        body.add("recipient", recipient);
        body.add("message", message);
        System.err.println(body);

        JsonObject excuteRequest =
                client.excuteRequest("https://openapi.zalo.me/v2.0/oa/message", "POST", null, body, headers, null);

        System.err.println(excuteRequest);

        System.exit(0);
    }
}
