package com.programmingtechie.zalo_oa_service.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagResponse {
    private String id;
    private String name;
    private Integer quantity;
}
