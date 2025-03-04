package com.programmingtechie.appointment_service.dto.response.medical;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private String id;

    private String serviceName;

    private String description;

    private Double suggestedFee;

    private String serviceCategoryId;

    private Boolean status;

    private ServiceCategoryResponse serviceCategoryResponse;
}
