package com.programmingtechie.zalo_oa_service.dto.response.user;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZaloUserResponse {
    private String id;
    private String displayName;
    private String avatar;
    private Boolean status;
    private String chatLink;
    private List<TagResponse> tags;
}
