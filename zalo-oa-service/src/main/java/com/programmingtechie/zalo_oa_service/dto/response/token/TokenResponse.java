package com.programmingtechie.zalo_oa_service.dto.response.token;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String id;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime expires;
}
