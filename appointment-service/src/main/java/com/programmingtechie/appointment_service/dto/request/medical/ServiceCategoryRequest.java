package com.programmingtechie.appointment_service.dto.request.medical;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryRequest {
    private String categoryName;
}
