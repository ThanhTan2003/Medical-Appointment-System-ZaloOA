package com.programmingtechie.appointment_service.mapper.doctor;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;
import com.programmingtechie.appointment_service.enity.doctor.DoctorService;
import com.programmingtechie.appointment_service.mapper.ServiceMapper;

@Component
public class DoctorServiceMapper {

    private final DoctorMapper doctorMapper;
    private final ServiceMapper serviceMapper;

    public DoctorServiceMapper(DoctorMapper doctorMapper, ServiceMapper serviceMapper) {
        this.doctorMapper = doctorMapper;
        this.serviceMapper = serviceMapper;
    }

    public DoctorServiceResponse toDoctorServiceResponse(DoctorService doctorService) {
        return DoctorServiceResponse.builder()
                .id(doctorService.getId())
                .doctorId(doctorService.getDoctor().getId())
                .serviceId(doctorService.getService().getId())
                .serviceFee(doctorService.getServiceFee())
                .status(doctorService.getStatus())
                .doctorResponse(doctorMapper.toDoctorResponse(doctorService.getDoctor()))
                .serviceResponse(serviceMapper.toServiceResponse(doctorService.getService()))
                .build();
    }
}
