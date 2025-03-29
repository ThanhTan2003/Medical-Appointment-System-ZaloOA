package com.programmingtechie.appointment_service.dto.request.medical;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    private String serviceName;

    private String description;

    private String price;

    private String serviceCategoryId;

    private Boolean status;
}
