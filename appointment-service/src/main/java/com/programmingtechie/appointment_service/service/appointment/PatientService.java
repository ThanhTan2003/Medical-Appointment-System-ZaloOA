package com.programmingtechie.appointment_service.service.appointment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.appointment.PatientRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.PatientResponse;
import com.programmingtechie.appointment_service.enity.appointment.Patient;
import com.programmingtechie.appointment_service.mapper.appointment.PatientMapper;
import com.programmingtechie.appointment_service.repository.appointment.PatientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    final PatientRepository patientRepository;
    final PatientMapper patientMapper;

    private void validatePatientRequest(PatientRequest patientRequest) {
        if (patientRequest == null || patientRequest.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên bệnh nhân không được để trống!");
        }
        if (patientRequest.getPhoneNumber() == null
                || patientRequest.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        if (patientRequest.getZaloUid() == null
                || patientRequest.getZaloUid().trim().isEmpty()) {
            throw new IllegalArgumentException("Zalo UID không được để trống!");
        }
    }

    public PageResponse<PatientResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findAll(pageable);

        List<PatientResponse> patientResponses = pageData.getContent().stream()
                .map(patientMapper::toPatientResponse)
                .collect(Collectors.toList());

        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(patientResponses)
                .build();
    }

    public ResponseEntity<PatientResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã bệnh nhân!");
        }

        return patientRepository
                .findById(id)
                .map(patient -> ResponseEntity.ok(patientMapper.toPatientResponse(patient)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân có mã " + id + "!"));
    }

    public ResponseEntity<PatientResponse> create(PatientRequest patientRequest) {
        validatePatientRequest(patientRequest);

        if (patientRepository.existsByPhoneNumber(patientRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại!");
        }
        if (patientRequest.getIdentityCard() != null
                && patientRepository.existsByIdentityCard(patientRequest.getIdentityCard())) {
            throw new IllegalArgumentException("Số CCCD đã tồn tại!");
        }

        Patient patient = Patient.builder()
                .id(UUID.randomUUID().toString())
                .zaloUid(patientRequest.getZaloUid())
                .fullName(patientRequest.getFullName())
                .gender(patientRequest.getGender())
                .phoneNumber(patientRequest.getPhoneNumber())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .identityCard(patientRequest.getIdentityCard())
                .address(patientRequest.getAddress())
                .notes(patientRequest.getNotes())
                .build();

        patient = patientRepository.save(patient);
        return ResponseEntity.ok(patientMapper.toPatientResponse(patient));
    }

    public ResponseEntity<PatientResponse> updateById(String id, PatientRequest patientRequest) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã bệnh nhân!");
        }
        validatePatientRequest(patientRequest);

        Patient existingPatient = patientRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân có mã " + id + "!"));

        boolean isUpdated = false;

        if (!existingPatient.getFullName().equals(patientRequest.getFullName())) {
            existingPatient.setFullName(patientRequest.getFullName());
            isUpdated = true;
        }
        if (!existingPatient.getPhoneNumber().equals(patientRequest.getPhoneNumber())) {
            existingPatient.setPhoneNumber(patientRequest.getPhoneNumber());
            isUpdated = true;
        }
        if (!existingPatient.getZaloUid().equals(patientRequest.getZaloUid())) {
            existingPatient.setZaloUid(patientRequest.getZaloUid());
            isUpdated = true;
        }
        if (!existingPatient.getGender().equals(patientRequest.getGender())) {
            existingPatient.setGender(patientRequest.getGender());
            isUpdated = true;
        }
        if (!existingPatient.getAddress().equals(patientRequest.getAddress())) {
            existingPatient.setAddress(patientRequest.getAddress());
            isUpdated = true;
        }
        if (!existingPatient.getNotes().equals(patientRequest.getNotes())) {
            existingPatient.setNotes(patientRequest.getNotes());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(patientMapper.toPatientResponse(existingPatient));
        }

        existingPatient = patientRepository.save(existingPatient);
        return ResponseEntity.ok(patientMapper.toPatientResponse(existingPatient));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã bệnh nhân!");
        }

        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy bệnh nhân có mã " + id + "!");
        }

        patientRepository.deleteById(id);
        return ResponseEntity.ok("Bệnh nhân có mã " + id + " đã được xóa thành công.");
    }
}
