package com.programmingtechie.zalo_oa_service.service.sendMessage;

import com.google.gson.JsonObject;
import com.programmingtechie.zalo_oa_service.config.ZaloConfig;
import com.programmingtechie.zalo_oa_service.oa.ZaloOaClient;
import com.programmingtechie.zalo_oa_service.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    final ZaloConfig zaloConfig;
    final TokenService tokenService;

    public void sendMessage(String userId, String message)
    {
        try{
            ZaloOaClient client = new ZaloOaClient();
            String access_token = tokenService.getTokenResponse().getAccessToken();

            Map<String, String> headers = new HashMap<>();
            headers.put("access_token", access_token);

            JsonObject id = new JsonObject();
            id.addProperty("user_id", userId);

            JsonObject text = new JsonObject();
            text.addProperty("text", message);

            JsonObject body = new JsonObject();
            body.add("recipient", id);
            body.add("message", text);

            JsonObject excuteRequest =
                    client.excuteRequest("https://openapi.zalo.me/v2.0/oa/message", "POST", null, body, headers, null);
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }
}
