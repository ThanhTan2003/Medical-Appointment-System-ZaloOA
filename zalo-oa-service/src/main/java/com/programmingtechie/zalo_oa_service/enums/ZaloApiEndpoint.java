package com.programmingtechie.zalo_oa_service.enums;

import lombok.Getter;

@Getter
public enum ZaloApiEndpoint {
    GET_ACCESS_TOKEN("https://oauth.zaloapp.com/v4/oa/access_token"),
    GET_REFRESH_TOKEN("https://oauth.zaloapp.com/v4/oa/access_token"),

    GET_USER_PROFILE("https://graph.zalo.me/v2.0/me"),
    SEND_MESSAGE_TRANSACTION("https://openapi.zalo.me/v3.0/oa/message/transaction"),
    UPLOAD_IMAGE("https://graph.zalo.me/v2.0/media/upload"),

    GET_LIST_FOLLOWER("https://openapi.zalo.me/v3.0/oa/user/getlist"),
    GET_FOLLOWER_INFO("https://openapi.zalo.me/v3.0/oa/user/detail"),

    CHAT("https://oa.zalo.me/chatv2");
    private final String url;

    ZaloApiEndpoint(String url) {
        this.url = url;
    }
}
