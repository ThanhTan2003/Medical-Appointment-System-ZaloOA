package com.programmingtechie.appointment_service.service.doctor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorResponse;
import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.mapper.doctor.DoctorMapper;
import com.programmingtechie.appointment_service.repository.doctor.DoctorRepository;
import com.programmingtechie.appointment_service.utils.DoctorUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {
    final DoctorRepository doctorRepository;
    final DoctorMapper doctorMapper;
    final DoctorUtil doctorUtil;

    private boolean isEmpty(String value) {
        return (value == null || value.trim().isEmpty());
    }

    private void validateId(String id) {
        if (isEmpty(id)) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã bác sĩ!");
        }
    }

    private void validateDoctorRequest(DoctorRequest doctorRequest) {
        if (doctorRequest == null) {
            throw new IllegalArgumentException("Dữ liệu bác sĩ không được để trống!");
        }
        if (isEmpty(doctorRequest.getName())) {
            throw new IllegalArgumentException("Tên bác sĩ không được để trống!");
        }
        if (isEmpty(doctorRequest.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại không được để trống!");
        }
        if (isEmpty(doctorRequest.getGender())) {
            throw new IllegalArgumentException("Giới tính không được để trống!");
        }
        if (doctorRequest.getStatus() == null) {
            throw new IllegalArgumentException("Trạng thái hoạt động không được để trống!");
        }
    }

    public PageResponse<DoctorResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.getAllDoctor(pageable);

        List<DoctorResponse> doctorResponses = pageData.getContent().stream()
                .map(doctorMapper::toDoctorResponse)
                .collect(Collectors.toList());

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(doctorResponses)
                .build();
    }

    public ResponseEntity<DoctorResponse> getById(String id) {
        validateId(id);
        return doctorRepository
                .findById(id)
                .map(doctor -> ResponseEntity.ok(doctorMapper.toDoctorResponse(doctor)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ có mã " + id + "!"));
    }

    public ResponseEntity<DoctorResponse> create(DoctorRequest doctorRequest) {
        validateDoctorRequest(doctorRequest);

        if (doctorRepository.existsByPhone(doctorRequest.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã tồn tại!");
        }

        String id = doctorUtil.generateDoctorId();

        Doctor doctor = Doctor.builder()
                .id(id)
                .name(doctorRequest.getName())
                .phone(doctorRequest.getPhone())
                .gender(doctorRequest.getGender())
                .description(doctorRequest.getDescription())
                .status(doctorRequest.getStatus())
                .build();

        doctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(doctorMapper.toDoctorResponse(doctor));
    }

    public ResponseEntity<DoctorResponse> updateById(String id, DoctorRequest doctorRequest) {
        validateId(id);
        validateDoctorRequest(doctorRequest);

        Doctor existingDoctor = doctorRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ có mã " + id + "!"));

        boolean isUpdated = false;

        if (!existingDoctor.getName().equals(doctorRequest.getName())) {
            existingDoctor.setName(doctorRequest.getName());
            isUpdated = true;
        }
        if (!existingDoctor.getPhone().equals(doctorRequest.getPhone())) {
            existingDoctor.setPhone(doctorRequest.getPhone());
            isUpdated = true;
        }
        if (!existingDoctor.getGender().equals(doctorRequest.getGender())) {
            existingDoctor.setGender(doctorRequest.getGender());
            isUpdated = true;
        }
        if (!existingDoctor.getDescription().equals(doctorRequest.getDescription())) {
            existingDoctor.setDescription(doctorRequest.getDescription());
            isUpdated = true;
        }
        if (!existingDoctor.getStatus().equals(doctorRequest.getStatus())) {
            existingDoctor.setStatus(doctorRequest.getStatus());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(doctorMapper.toDoctorResponse(existingDoctor));
        }

        existingDoctor = doctorRepository.save(existingDoctor);
        return ResponseEntity.ok(doctorMapper.toDoctorResponse(existingDoctor));
    }

    public ResponseEntity<String> deleteById(String id) {
        validateId(id);

        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy bác sĩ có mã " + id + "!");
        }

        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Không thể xóa thông tin bác sĩ!");
        }
        return ResponseEntity.ok("Bác sĩ có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Doctor> doctorPage = doctorRepository.searchDoctorsWithUnaccent(keyword, pageable);

        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(doctorMapper::toDoctorResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                doctorPage.getTotalPages(), page, size, doctorPage.getTotalElements(), doctorResponses);
    }

    public PageResponse<DoctorResponse> searchDoctorsByStatus(Boolean status, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Doctor> doctorPage = doctorRepository.findByStatus(status, pageable);

        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(doctorMapper::toDoctorResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                doctorPage.getTotalPages(), page, size, doctorPage.getTotalElements(), doctorResponses);
    }

    public PageResponse<DoctorResponse> searchByService(String keyword, String serviceId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<Doctor> doctorPage = doctorRepository.findDoctorsByServiceId(keyword, serviceId, pageable);

        List<DoctorResponse> doctorResponses = doctorPage.getContent().stream()
                .map(doctorMapper::toDoctorResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                doctorPage.getTotalPages(), page, size, doctorPage.getTotalElements(), doctorResponses);
    }
}
