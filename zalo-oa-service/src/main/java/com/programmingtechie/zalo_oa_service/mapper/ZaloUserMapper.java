package com.programmingtechie.zalo_oa_service.mapper;

import com.programmingtechie.zalo_oa_service.config.ZaloConfig;
import com.programmingtechie.zalo_oa_service.enums.ZaloApiEndpoint;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.programmingtechie.zalo_oa_service.dto.response.user.ZaloUserResponse;
import com.programmingtechie.zalo_oa_service.entity.ZaloUser;
import com.programmingtechie.zalo_oa_service.service.user.ZaloUserTagService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ZaloUserMapper {
    @Lazy
    final ZaloUserTagService zaloUserTagService;
    final ZaloConfig zaloConfig;

    public ZaloUserResponse toResponse(ZaloUser zaloUser) {
        return ZaloUserResponse.builder()
                .id(zaloUser.getId())
                .displayName(zaloUser.getDisplayName())
                .avatar(zaloUser.getAvatar())
                .status(zaloUser.getStatus())
                .chatLink(getChatLink(zaloUser.getId()))
                .tags(zaloUserTagService.getTagsByUserId(zaloUser.getId()))
                .build();
    }
    String getChatLink(String userId)
    {
        return ZaloApiEndpoint.CHAT.getUrl() + "?uid=" + userId + "&oaid=" + zaloConfig.getOaId();
    }
}
