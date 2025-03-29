package com.programmingtechie.appointment_service.mapper.medical;

import com.programmingtechie.appointment_service.enums.IconURLAddress;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceCategoryResponse;
import com.programmingtechie.appointment_service.enity.medical.ServiceCategory;

@Component
public class ServiceCategoryMapper {
    public ServiceCategoryResponse toServiceCategoryResponse(ServiceCategory serviceCategory) {
        String img = serviceCategory.getImage();
        if(img == null || img.isEmpty())
            img = IconURLAddress.DEFAULT_MEDICAL.getUrl();
        return ServiceCategoryResponse.builder()
                .id(serviceCategory.getId())
                .categoryName(serviceCategory.getCategoryName())
                .description(serviceCategory.getDescription())
                .quantity(serviceCategory.getServices().size())
                .image(img)
                .build();
    }
}