package com.programmingtechie.appointment_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceResponse;
import com.programmingtechie.appointment_service.enity.medical.Service;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceMapper {
    ServiceCategoryMapper serviceCategoryMapper;

    public ServiceResponse toServiceResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .suggestedFee(service.getSuggestedFee())
                .serviceCategoryId(service.getServiceCategory().getId())
                .status(service.getStatus())
                .serviceCategoryResponse(serviceCategoryMapper.toServiceCategoryResponse(service.getServiceCategory()))
                .build();
    }
}
