package com.programmingtechie.appointment_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceCategoryResponse;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;

@Component
public class ServiceCategoryMapper {
    public ServiceCategoryResponse toServiceCategoryResponse(ServiceCategory serviceCategory) {
        return ServiceCategoryResponse.builder()
                .id(serviceCategory.getId())
                .categoryName(serviceCategory.getCategoryName())
                .build();
    }
}
