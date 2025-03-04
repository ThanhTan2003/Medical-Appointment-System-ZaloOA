package com.programmingtechie.zalo_oa_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.zalo_oa_service.config.ZaloConfig;
import com.programmingtechie.zalo_oa_service.dto.response.RefreshTokenResponse;
import com.programmingtechie.zalo_oa_service.dto.response.TokenResponse;
import com.programmingtechie.zalo_oa_service.entity.Token;
import com.programmingtechie.zalo_oa_service.mapper.TokenMapper;
import com.programmingtechie.zalo_oa_service.repository.TokenRepository;
import com.programmingtechie.zalo_oa_service.utils.token.TokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    final TokenRepository tokenRepository;
    final TokenMapper tokenMapper;
    final ZaloConfig zaloOAConfig;
    final TokenUtil tokenUtil;

    public Token getToken() {
        List<Token> tokens = tokenRepository.findAll();
        if (tokens.isEmpty()) {
            return null;
        }
        return tokens.get(0);
    }

    TokenResponse updateToken(String accessToken, String refreshToken, LocalDateTime expires) {
        Token token = getToken();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpires(expires);

        tokenRepository.save(token);
        return tokenMapper.toTokenResponse(token);
    }

    @Scheduled(fixedRate = 3600000)
    public void checkTokenExpirationAndRefreshToken() {
        TokenResponse token = getTokenResponse();
        if (token == null) return;
        LocalDateTime expires = token.getExpires();
        log.info(("Check token expiration"));

        LocalDateTime currentTimePlusOneHourFifteenMinutes =
                LocalDateTime.now().plusHours(1).plusMinutes(15);

        if (currentTimePlusOneHourFifteenMinutes.isAfter(expires)
                || currentTimePlusOneHourFifteenMinutes.isEqual(expires)) {
            refreshToken();
        } else {
            return;
        }
    }

    public TokenResponse getTokenResponse() {
        Token token = getToken();
        if (token == null) return null;
        return tokenMapper.toTokenResponse(token);
    }

    @Transactional
    public TokenResponse refreshToken() {
        log.info("perform Token refresh...");
        Token token = getToken();
        LocalDateTime expires = null;
        TokenResponse tokenResponse = null;
        try {
            String refreshToken = token.getRefreshToken();

            RefreshTokenResponse response = tokenUtil.getAccessTokenByRefreshToken(
                    refreshToken, zaloOAConfig.getAppId(), zaloOAConfig.getSecretKey());
            log.info("Refresh token result: {}", response);

            String expiresIn = response.getExpires_in();
            if (expiresIn != null && !expiresIn.isEmpty()) {
                try {
                    long expiresInSeconds = Long.parseLong(expiresIn);
                    expires = LocalDateTime.now().plusSeconds(expiresInSeconds);
                    log.info("Token expires at: {}", expires);
                } catch (NumberFormatException e) {
                    log.error("Invalid expires_in value: {}", expiresIn, e);
                }
            } else {
                log.warn("expires_in is null or empty");
            }

            if (expires != null) {
                tokenResponse = updateToken(response.getAccess_token(), response.getRefresh_token(), expires);
            } else {
                log.error("Expires time is null. Token update failed.");
            }

        } catch (Exception e) {
            log.error("An error occurred while refreshing the token", e);
            throw new IllegalArgumentException("Đã xảy ra lỗi: " + e.toString());
        }
        return tokenResponse;
    }

    @Transactional
    public TokenResponse generateToken() {
        Token token = getToken();
        LocalDateTime expires = null;
        try {

            RefreshTokenResponse response = tokenUtil.generateToken(
                    zaloOAConfig.getAuthorizationCode(), zaloOAConfig.getAppId(), zaloOAConfig.getSecretKey());
            log.info("Refresh token result: {}", response);

            String expiresIn = response.getExpires_in();
            if (expiresIn != null && !expiresIn.isEmpty()) {
                try {
                    long expiresInSeconds = Long.parseLong(expiresIn);
                    expires = LocalDateTime.now().plusSeconds(expiresInSeconds);
                    log.info("Token expires at: {}", expires);
                } catch (NumberFormatException e) {
                    log.error("Invalid expires_in value: {}", expiresIn, e);
                }
            } else {
                log.warn("expires_in is null or empty");
            }

            if (expires != null) {
                updateToken(response.getAccess_token(), response.getRefresh_token(), expires);
            } else {
                log.error("Expires time is null. Token update failed.");
            }

        } catch (Exception e) {
            log.error("An error occurred while refreshing the token", e);
            throw new IllegalArgumentException("Đã xảy ra lỗi: " + e.toString());
        }
        return null;
    }

    public TokenResponse generateTokenByAuthorizationCode(String authorizationCode) {
        Token token = getToken();
        LocalDateTime expires = null;
        try {

            log.info("chay......");
            RefreshTokenResponse response =
                    tokenUtil.generateToken(authorizationCode, zaloOAConfig.getAppId(), zaloOAConfig.getSecretKey());
            log.info("Refresh token result: {}", response);

            String expiresIn = response.getExpires_in();
            if (expiresIn != null && !expiresIn.isEmpty()) {
                try {
                    long expiresInSeconds = Long.parseLong(expiresIn);
                    expires = LocalDateTime.now().plusSeconds(expiresInSeconds);
                    log.info("Token expires at: {}", expires);
                } catch (NumberFormatException e) {
                    log.error("Invalid expires_in value: {}", expiresIn, e);
                }
            } else {
                log.warn("expires_in is null or empty");
            }

            if (expires != null) {
                if (token == null) {
                    token = Token.builder()
                            .id(UUID.randomUUID().toString())
                            .accessToken(response.getAccess_token())
                            .refreshToken(response.getRefresh_token())
                            .expires(expires)
                            .build();
                    tokenRepository.save(token);
                } else updateToken(response.getAccess_token(), response.getRefresh_token(), expires);
            } else {
                log.error("Expires time is null. Token update failed.");
            }

        } catch (Exception e) {
            log.error("An error occurred while refreshing the token", e);
            throw new IllegalArgumentException("Đã xảy ra lỗi: " + e.toString());
        }
        return tokenMapper.toTokenResponse(token);
    }
}
