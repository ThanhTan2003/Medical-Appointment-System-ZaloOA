package com.programmingtechie.zalo_oa_service.dto.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagRequest {
    private String name;
}
