package com.programmingtechie.zalo_oa_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefreshTokenResponse {
    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("expires_in")
    private String expires_in;
}
