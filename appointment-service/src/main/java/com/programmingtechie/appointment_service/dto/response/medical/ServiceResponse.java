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

    private String price;

    private String serviceCategoryId;

    private Boolean status;

    private String image;

    private ServiceCategoryResponse serviceCategoryResponse;
}
