package com.programmingtechie.zalo_oa_service.dto.response.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccessTokenErrorResponse {
    String error_name;
    String error_reason;
    String ref_doc;
    String error_description;
    Long error;
}
