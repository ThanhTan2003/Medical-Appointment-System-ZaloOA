package com.programmingtechie.zalo_oa_service.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ZaloUserRequest {
    private String id;
    private String displayName;
    private String avatar;
    private Boolean status;
}
