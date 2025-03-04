package com.programmingtechie.appointment_service.service.doctor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorScheduleRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorScheduleResponse;
import com.programmingtechie.appointment_service.enity.doctor.Doctor;
import com.programmingtechie.appointment_service.enity.doctor.DoctorSchedule;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;
import com.programmingtechie.appointment_service.mapper.doctor.DoctorScheduleMapper;
import com.programmingtechie.appointment_service.repository.appointment.TimeFrameRepository;
import com.programmingtechie.appointment_service.repository.doctor.DoctorRepository;
import com.programmingtechie.appointment_service.repository.doctor.DoctorScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorScheduleService {
    final DoctorScheduleRepository doctorScheduleRepository;
    final DoctorRepository doctorRepository;
    final TimeFrameRepository timeFrameRepository;
    final DoctorScheduleMapper doctorScheduleMapper;

    private void validateDoctorScheduleRequest(DoctorScheduleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu lịch bác sĩ không được để trống!");
        }
        if (request.getDoctorId() == null || request.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bác sĩ không được để trống!");
        }
        if (request.getTimeFrameId() == null || request.getTimeFrameId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khung giờ không được để trống!");
        }
        if (request.getMaxPatients() == null || request.getMaxPatients() <= 0) {
            throw new IllegalArgumentException("Số lượng bệnh nhân tối đa phải lớn hơn 0!");
        }
    }

    public ResponseEntity<DoctorScheduleResponse> create(DoctorScheduleRequest request) {
        validateDoctorScheduleRequest(request);

        // Kiểm tra sự tồn tại của Doctor và TimeFrame
        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ!"));

        TimeFrame timeFrame = timeFrameRepository
                .findById(request.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ!"));

        DoctorSchedule doctorSchedule = DoctorSchedule.builder()
                .id(UUID.randomUUID().toString())
                .doctor(doctor)
                .timeFrame(timeFrame)
                .dayOfWeek(request.getDayOfWeek())
                .maxPatients(request.getMaxPatients())
                .status(request.getStatus())
                .build();

        doctorSchedule = doctorScheduleRepository.save(doctorSchedule);
        return ResponseEntity.ok(doctorScheduleMapper.toDoctorScheduleResponse(doctorSchedule));
    }

    public PageResponse<DoctorScheduleResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorSchedule> pageData = doctorScheduleRepository.findAll(pageable);

        List<DoctorScheduleResponse> responses = pageData.getContent().stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public ResponseEntity<DoctorScheduleResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch bác sĩ!");
        }

        return doctorScheduleRepository
                .findById(id)
                .map(doctorSchedule -> ResponseEntity.ok(doctorScheduleMapper.toDoctorScheduleResponse(doctorSchedule)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch bác sĩ có mã " + id + "!"));
    }

    public ResponseEntity<DoctorScheduleResponse> updateById(String id, DoctorScheduleRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch bác sĩ!");
        }

        validateDoctorScheduleRequest(request);

        DoctorSchedule existingDoctorSchedule = doctorScheduleRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch bác sĩ có mã " + id + "!"));

        // Kiểm tra nếu Doctor và TimeFrame có thay đổi
        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ!"));

        TimeFrame timeFrame = timeFrameRepository
                .findById(request.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ!"));

        existingDoctorSchedule.setDoctor(doctor);
        existingDoctorSchedule.setTimeFrame(timeFrame);
        existingDoctorSchedule.setDayOfWeek(request.getDayOfWeek());
        existingDoctorSchedule.setMaxPatients(request.getMaxPatients());
        existingDoctorSchedule.setStatus(request.getStatus());

        existingDoctorSchedule = doctorScheduleRepository.save(existingDoctorSchedule);
        return ResponseEntity.ok(doctorScheduleMapper.toDoctorScheduleResponse(existingDoctorSchedule));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch bác sĩ!");
        }

        if (!doctorScheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy lịch bác sĩ có mã " + id + "!");
        }

        doctorScheduleRepository.deleteById(id);
        return ResponseEntity.ok("Lịch bác sĩ có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<DoctorScheduleResponse> getByDoctorId(String doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorSchedule> pageData = doctorScheduleRepository.findByDoctorIdOrderByDayOfWeekAsc(doctorId, pageable);

        List<DoctorScheduleResponse> responses = pageData.getContent().stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<DoctorScheduleResponse> getByTimeFrameId(String timeFrameId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorSchedule> pageData =
                doctorScheduleRepository.findByTimeFrameIdOrderByDayOfWeekAsc(timeFrameId, pageable);

        List<DoctorScheduleResponse> responses = pageData.getContent().stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<DoctorScheduleResponse> getByStatus(Boolean status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorSchedule> pageData = doctorScheduleRepository.findByStatus(status, pageable);

        List<DoctorScheduleResponse> responses = pageData.getContent().stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }
}
