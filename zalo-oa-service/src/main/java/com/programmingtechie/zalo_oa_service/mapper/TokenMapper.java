package com.programmingtechie.zalo_oa_service.mapper;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.programmingtechie.zalo_oa_service.dto.response.token.AccessTokenResponse;
import com.programmingtechie.zalo_oa_service.dto.response.token.RefreshTokenResponse;
import com.programmingtechie.zalo_oa_service.dto.response.token.TokenResponse;
import com.programmingtechie.zalo_oa_service.entity.Token;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenMapper {
    public TokenResponse toTokenResponse(Token token) {
        if (token == null) return null;
        return TokenResponse.builder()
                .id(token.getId())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expires(token.getExpires())
                .build();
    }

    public RefreshTokenResponse toRefreshTokenResponse(JsonObject jsonObject) {
        return RefreshTokenResponse.builder()
                .access_token(jsonObject.get("access_token").getAsString())
                .refresh_token(jsonObject.get("refresh_token").getAsString())
                .expires_in(jsonObject.get("expires_in").getAsString())
                .build();
    }

    public AccessTokenResponse toAccessTokenResponse(JsonObject jsonObject) {
        return AccessTokenResponse.builder()
                .access_token(jsonObject.get("access_token").getAsString())
                .refresh_token(jsonObject.get("refresh_token").getAsString())
                .expires_in(jsonObject.get("expires_in").getAsString())
                .build();
    }
}
