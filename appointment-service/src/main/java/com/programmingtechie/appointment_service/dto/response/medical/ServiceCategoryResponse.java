package com.programmingtechie.appointment_service.dto.response.medical;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryResponse {
    private String id;

    private String categoryName;

    private String image;

    private String description;

    private Integer quantity;
}
