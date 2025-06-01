package com.programmingtechie.appointment_service.mapper.medical;

import com.programmingtechie.appointment_service.enums.IconURLAddress;
import com.programmingtechie.appointment_service.repository.doctor.DoctorServiceRepository;
import com.programmingtechie.appointment_service.service.doctor.DoctorServiceService;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.medical.ServiceResponse;
import com.programmingtechie.appointment_service.enity.medical.Service;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceMapper {
    private final ServiceCategoryMapper serviceCategoryMapper;
    private final DoctorServiceRepository doctorServiceRepository;

    public ServiceResponse toServiceResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .price(service.getPrice())
                .serviceCategoryId(service.getServiceCategory().getId())
                .status(service.getStatus())
                .image(IconURLAddress.DEFAULT_MEDICAL.getUrl())
                .serviceCategoryResponse(serviceCategoryMapper.toServiceCategoryResponse(service.getServiceCategory()))
                .numberOfDoctors(countDoctorsByServiceId(service.getId()))
                .build();
    }

    public Long countDoctorsByServiceId(String serviceId) {
        return doctorServiceRepository.countActiveDoctoringByServiceId(serviceId);
    }
}