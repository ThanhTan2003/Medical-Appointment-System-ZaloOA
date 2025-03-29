package com.programmingtechie.appointment_service.service.appointment;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.programmingtechie.appointment_service.utils.PatientUtil;
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
    final PatientUtil patientUtil;

    String zaloUidDefault = "7546191973773392974";

    private void validatePatientRequest(PatientRequest patientRequest) {
        if (patientRequest == null || patientRequest.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống!");
        }

        if (patientRequest.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Giới tính không được để trống!");
        }

        if (patientRequest.getPhoneNumber() == null || patientRequest.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        } else if (!patientRequest.getPhoneNumber().trim().matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
        }

        if (patientRequest.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống!");
        }

        if (patientRequest.getIdentityCard() == null || patientRequest.getIdentityCard().trim().isEmpty()) {
            throw new IllegalArgumentException("Số căn cước không được để trống!");
        } else if (patientRequest.getIdentityCard().trim().length() != 12) {
            throw new IllegalArgumentException("Số căn cước không hợp lệ!");
        }

        if (patientRequest.getInsuranceId() != null && !Objects.equals(patientRequest.getInsuranceId(), "") && !patientRequest.getInsuranceId().trim().isEmpty()
                && patientRequest.getInsuranceId().trim().length() != 15) {
            throw new IllegalArgumentException("Số thẻ bảo hiểm y tế không hợp lệ!");
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
        String zaloUid = zaloUidDefault;
        if(Objects.equals(patientRequest.getInsuranceId(), ""))
            patientRequest.setInsuranceId(null);
        validatePatientRequest(patientRequest);

        if (patientRequest.getInsuranceId() != null
                && patientRepository.existsByInsuranceIdAndZaloUid(patientRequest.getInsuranceId(), zaloUid)) {
            throw new IllegalArgumentException("Số bảo hiểm y tế đã được sử dụng!");
        }

        if (patientRequest.getIdentityCard() != null
                && patientRepository.existsByIdentityCardAndZaloUid(patientRequest.getIdentityCard(), zaloUid)) {
            throw new IllegalArgumentException("Số căn cước đã tồn tại!");
        }

        Patient patient = Patient.builder()
                .id(patientUtil.generateId())
                .zaloUid(zaloUid)
                .fullName(patientRequest.getFullName())
                .gender(patientRequest.getGender())
                .phoneNumber(patientRequest.getPhoneNumber())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .identityCard(patientRequest.getIdentityCard())
                .address(patientRequest.getAddress())
                .insuranceId(patientRequest.getInsuranceId())
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
        if (!existingPatient.getGender().equals(patientRequest.getGender())) {
            existingPatient.setGender(patientRequest.getGender());
            isUpdated = true;
        }
        if (!existingPatient.getAddress().equals(patientRequest.getAddress())) {
            existingPatient.setAddress(patientRequest.getAddress());
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

    // Tìm danh sách patient theo zaloUid và keyword
    public PageResponse<PatientResponse> getPatients(String keyword, int page, int size) {
        String zaloUid = zaloUidDefault;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> patientPage = patientRepository.findByZaloUidAndKeyword(zaloUid, keyword, pageable);

        List<PatientResponse> patientResponses = patientPage.getContent().stream()
                .map(patientMapper::toPatientResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(patientPage.getTotalPages(), page, size, patientPage.getTotalElements(), patientResponses);
    }
}
