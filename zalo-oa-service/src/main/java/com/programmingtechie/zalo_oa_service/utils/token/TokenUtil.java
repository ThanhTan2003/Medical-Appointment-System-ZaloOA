package com.programmingtechie.zalo_oa_service.utils.token;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.programmingtechie.zalo_oa_service.dto.response.RefreshTokenResponse;
import com.programmingtechie.zalo_oa_service.enums.ZaloApiEndpoint;
import com.programmingtechie.zalo_oa_service.mapper.TokenMapper;
import com.programmingtechie.zalo_oa_service.oa.APIException;
import com.programmingtechie.zalo_oa_service.oa.ZaloBaseClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil {
    final TokenMapper tokenMapper;

    public RefreshTokenResponse getAccessTokenByRefreshToken(String refreshToken, String appId, String secretKey)
            throws APIException {
        RefreshTokenResponse refreshTokenResponse;
        ZaloBaseClient client = new ZaloBaseClient();

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                URIBuilder builder = new URIBuilder(ZaloApiEndpoint.GET_ACCESS_TOKEN.getUrl());

                HttpPost sendMethodPost = new HttpPost(builder.toString());
                Map<String, String> header = new HashMap<>();
                header.put("secret_key", secretKey);
                header.put("Content-Type", "application/x-www-form-urlencoded");

                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        sendMethodPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }

                Map<String, String> body = new HashMap<>();
                body.put("refresh_token", refreshToken);
                body.put("app_id", appId);
                body.put("grant_type", "refresh_token");

                List<NameValuePair> params = new ArrayList<>();
                for (String key : body.keySet()) {
                    params.add(new BasicNameValuePair(key, body.get(key)));
                }

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);
                sendMethodPost.setEntity(urlEncodedFormEntity);
                CloseableHttpResponse response = httpClient.execute(sendMethodPost);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String jsonResponse = EntityUtils.toString(entity);

                        JsonObject jsonObject =
                                JsonParser.parseString(jsonResponse).getAsJsonObject();
                        refreshTokenResponse = tokenMapper.toRefreshTokenResponse(jsonObject);
                    } else return null;
                } finally {
                    response.close();
                }

            } catch (IOException | URISyntaxException ex) {
                throw new APIException(ex);
            } finally {
                httpclient.close();
            }
        } catch (APIException | IOException | ParseException ex) {
            throw new APIException(ex);
        }
        return refreshTokenResponse;
    }

    public RefreshTokenResponse generateToken(String authorizationCode, String appId, String secretKey)
            throws APIException {
        RefreshTokenResponse refreshTokenResponse;
        ZaloBaseClient client = new ZaloBaseClient();

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                URIBuilder builder = new URIBuilder(ZaloApiEndpoint.GET_ACCESS_TOKEN.getUrl());

                HttpPost sendMethodPost = new HttpPost(builder.toString());
                Map<String, String> header = new HashMap<>();
                header.put("secret_key", secretKey);
                header.put("Content-Type", "application/x-www-form-urlencoded");

                if (header != null) {
                    for (Map.Entry<String, String> entry : header.entrySet()) {
                        sendMethodPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }

                Map<String, String> body = new HashMap<>();
                body.put("code", authorizationCode);
                body.put("app_id", appId);
                body.put("grant_type", "authorization_code");

                List<NameValuePair> params = new ArrayList<>();
                for (String key : body.keySet()) {
                    params.add(new BasicNameValuePair(key, body.get(key)));
                }

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);
                sendMethodPost.setEntity(urlEncodedFormEntity);
                CloseableHttpResponse response = httpClient.execute(sendMethodPost);
                try {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        String jsonResponse = EntityUtils.toString(entity);

                        JsonObject jsonObject =
                                JsonParser.parseString(jsonResponse).getAsJsonObject();

                        refreshTokenResponse = tokenMapper.toRefreshTokenResponse(jsonObject);
                    } else return null;
                } finally {
                    response.close();
                }

            } catch (IOException | URISyntaxException ex) {
                throw new APIException(ex);
            } finally {
                httpclient.close();
            }
        } catch (APIException | IOException | ParseException ex) {
            throw new APIException(ex);
        }
        return refreshTokenResponse;
    }
}
