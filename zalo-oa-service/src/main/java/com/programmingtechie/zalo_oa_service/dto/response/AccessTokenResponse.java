package com.programmingtechie.zalo_oa_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessTokenResponse {
    String access_token;
    String refresh_token;
    String expires_in;
}
