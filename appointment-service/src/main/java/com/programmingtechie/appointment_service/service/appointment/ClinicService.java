package com.programmingtechie.appointment_service.service.appointment;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.appointment.ClinicRequest;
import com.programmingtechie.appointment_service.dto.response.appointment.ClinicResponse;
import com.programmingtechie.appointment_service.enity.appointment.Clinic;
import com.programmingtechie.appointment_service.mapper.appointment.ClinicMapper;
import com.programmingtechie.appointment_service.repository.appointment.ClinicRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicService {
    final ClinicRepository clinicRepository;
    final ClinicMapper clinicMapper;

    private void validateClinicRequest(ClinicRequest clinicRequest) {
        if (clinicRequest == null || clinicRequest.getClinicName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng khám không được để trống!");
        }
        if (clinicRequest.getAddress() == null
                || clinicRequest.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống!");
        }
        if (clinicRequest.getSupportPhone() == null
                || clinicRequest.getSupportPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại hỗ trợ không được để trống!");
        }
    }

    public ResponseEntity<ClinicResponse> getClinicInfo() {
        Optional<Clinic> clinicOptional = clinicRepository.findAll().stream().findFirst();

        if (clinicOptional.isEmpty()) {
            throw new IllegalArgumentException("Chưa có thông tin phòng khám, vui lòng tạo mới!");
        }

        return ResponseEntity.ok(clinicMapper.toClinicResponse(clinicOptional.get()));
    }

    public ResponseEntity<ClinicResponse> createOrUpdateClinic(ClinicRequest clinicRequest) {
        validateClinicRequest(clinicRequest);

        Optional<Clinic> existingClinic = clinicRepository.findAll().stream().findFirst();

        Clinic clinic;
        if (existingClinic.isPresent()) {
            clinic = existingClinic.get();
            clinic.setClinicName(clinicRequest.getClinicName());
            clinic.setAddress(clinicRequest.getAddress());
            clinic.setDescription(clinicRequest.getDescription());
            clinic.setSupportPhone(clinicRequest.getSupportPhone());
        } else {
            clinic = Clinic.builder()
                    .id(UUID.randomUUID().toString())
                    .clinicName(clinicRequest.getClinicName())
                    .address(clinicRequest.getAddress())
                    .description(clinicRequest.getDescription())
                    .supportPhone(clinicRequest.getSupportPhone())
                    .build();
        }

        clinic = clinicRepository.save(clinic);
        return ResponseEntity.ok(clinicMapper.toClinicResponse(clinic));
    }
}
