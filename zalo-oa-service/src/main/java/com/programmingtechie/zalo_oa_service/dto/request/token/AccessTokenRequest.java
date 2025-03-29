package com.programmingtechie.zalo_oa_service.dto.request.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessTokenRequest {
    String code;
    Long app_id;
    String grant_type;
}
