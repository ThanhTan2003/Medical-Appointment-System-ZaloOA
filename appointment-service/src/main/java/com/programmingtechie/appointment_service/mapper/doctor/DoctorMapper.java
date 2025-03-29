package com.programmingtechie.appointment_service.mapper.doctor;

import com.programmingtechie.appointment_service.repository.doctor.DoctorServiceRepository;
import com.programmingtechie.appointment_service.repository.medical.ServiceCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorResponse;
import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enums.DoctorStatus;

import java.util.List;

@Component
@AllArgsConstructor
public class DoctorMapper {
    final ServiceCategoryRepository serviceCategoryRepository;
    public DoctorResponse toDoctorResponse(Doctor doctor) {
        DoctorResponse doctorResponse = DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .zaloUid(doctor.getZaloUid())
                .phone(doctor.getPhone())
                .gender(doctor.getGender())
                .description(doctor.getDescription())
                .status(doctor.getStatus())
                .image(doctor.getImage())
                .nameOfServiceCategory(getNameOfServiceCategory(doctor.getId()))
                .build();

        DoctorStatus statusEnum = DoctorStatus.fromBoolean(doctor.getStatus());
        if (statusEnum != null) {
            doctorResponse.setStatusName(statusEnum.getDescription());
        }

        return doctorResponse;
    }

    public String getNameOfServiceCategory(String doctorId) {
        List<String> categoryNames = serviceCategoryRepository.findServiceCategoryNamesByDoctorId(doctorId);
        return String.join(", ", categoryNames);
    }

}
