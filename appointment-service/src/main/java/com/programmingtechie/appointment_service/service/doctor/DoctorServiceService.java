package com.programmingtechie.appointment_service.service.doctor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorServiceRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;
import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enity.doctor.DoctorService;
import com.programmingtechie.appointment_service.mapper.doctor.DoctorServiceMapper;
import com.programmingtechie.appointment_service.repository.doctor.DoctorRepository;
import com.programmingtechie.appointment_service.repository.doctor.DoctorServiceRepository;
import com.programmingtechie.appointment_service.repository.medical.ServiceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceService {
    final DoctorServiceRepository doctorServiceRepository;
    final DoctorRepository doctorRepository;
    final ServiceRepository serviceRepository;
    final DoctorServiceMapper doctorServiceMapper;

    private void validateDoctorServiceRequest(DoctorServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu dịch vụ bác sĩ không được để trống!");
        }
        if (request.getDoctorId() == null || request.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bác sĩ không được để trống!");
        }
        if (request.getServiceId() == null || request.getServiceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã dịch vụ không được để trống!");
        }
        if (request.getServiceFee() == null || request.getServiceFee() <= 0) {
            throw new IllegalArgumentException("Phí dịch vụ phải lớn hơn 0!");
        }
    }

    public ResponseEntity<DoctorServiceResponse> create(DoctorServiceRequest request) {
        validateDoctorServiceRequest(request);

        // Kiểm tra sự tồn tại của Doctor và Service
        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ!"));

        com.programmingtechie.appointment_service.enity.medical.Service service = serviceRepository
                .findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ!"));

        DoctorService doctorService = DoctorService.builder()
                .id(UUID.randomUUID().toString())
                .doctor(doctor)
                .service(service)
                .serviceFee(request.getServiceFee())
                .status(request.getStatus())
                .build();

        doctorService = doctorServiceRepository.save(doctorService);
        return ResponseEntity.ok(doctorServiceMapper.toDoctorServiceResponse(doctorService));
    }

    public PageResponse<DoctorServiceResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> pageData = doctorServiceRepository.findAll(pageable);

        List<DoctorServiceResponse> responses = pageData.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public ResponseEntity<DoctorServiceResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ bác sĩ!");
        }

        return doctorServiceRepository
                .findById(id)
                .map(doctorService -> ResponseEntity.ok(doctorServiceMapper.toDoctorServiceResponse(doctorService)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ có mã " + id + "!"));
    }

    public ResponseEntity<DoctorServiceResponse> updateById(String id, DoctorServiceRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ bác sĩ!");
        }

        validateDoctorServiceRequest(request);

        DoctorService existingDoctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ có mã " + id + "!"));

        // Kiểm tra nếu Doctor và Service có thay đổi
        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ!"));

        com.programmingtechie.appointment_service.enity.medical.Service service = serviceRepository
                .findById(request.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ!"));

        existingDoctorService.setDoctor(doctor);
        existingDoctorService.setService(service);
        existingDoctorService.setServiceFee(request.getServiceFee());
        existingDoctorService.setStatus(request.getStatus());

        existingDoctorService = doctorServiceRepository.save(existingDoctorService);
        return ResponseEntity.ok(doctorServiceMapper.toDoctorServiceResponse(existingDoctorService));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã dịch vụ bác sĩ!");
        }

        if (!doctorServiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ có mã " + id + "!");
        }

        doctorServiceRepository.deleteById(id);
        return ResponseEntity.ok("Dịch vụ bác sĩ có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<DoctorServiceResponse> getByDoctorId(String doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> pageData = doctorServiceRepository.findByDoctorIdOrderByServiceFeeDesc(doctorId, pageable);

        List<DoctorServiceResponse> responses = pageData.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<DoctorServiceResponse> getByServiceId(String serviceId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> pageData =
                doctorServiceRepository.findByServiceIdOrderByServiceFeeDesc(serviceId, pageable);

        List<DoctorServiceResponse> responses = pageData.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<DoctorServiceResponse> getByStatus(Boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> pageData = doctorServiceRepository.findByStatus(status, pageable);

        List<DoctorServiceResponse> responses = pageData.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }
}
