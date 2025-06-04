package com.programmingtechie.appointment_service.service.doctor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorScheduleStatusResponse;
import com.programmingtechie.appointment_service.enity.appointment.Appointment;
import com.programmingtechie.appointment_service.enums.DoctorScheduleStatus;
import com.programmingtechie.appointment_service.repository.appointment.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    final AppointmentRepository appointmentRepository;
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

    @Transactional
    public ResponseEntity<DoctorScheduleResponse> create(DoctorScheduleRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu lịch bác sĩ không được để trống!");
        }
        if (request.getDoctorId() == null || request.getDoctorId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bác sĩ không được để trống!");
        }
        if (request.getTimeFrameId() == null || request.getTimeFrameId().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã khung giờ không được để trống!");
        }
        if (request.getMaxPatients() == null) {
            throw new IllegalArgumentException("Số lượng bệnh nhân tối đa không được để trống!");
        }

        // Kiểm tra sự tồn tại của Doctor và TimeFrame
        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ!"));

        TimeFrame timeFrame = timeFrameRepository
                .findById(request.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ!"));

        // Kiểm tra xem đã có lịch với doctor, timeframe và dayOfWeek này chưa
        Optional<DoctorSchedule> existingSchedule = doctorScheduleRepository
                .findByDoctorAndTimeFrameAndDayOfWeek(doctor, timeFrame, request.getDayOfWeek());

        DoctorSchedule doctorSchedule;
        if (existingSchedule.isPresent()) {
            // Cập nhật lịch hiện có
            doctorSchedule = existingSchedule.get();
            doctorSchedule.setMaxPatients(request.getMaxPatients());
            doctorSchedule.setStatus(request.getMaxPatients() > 0);
        } else {
            // Chỉ tạo mới nếu maxPatients > 0
            if (request.getMaxPatients() <= 0) {
                throw new IllegalArgumentException("Không thể tạo mới lịch với số lượng bệnh nhân bằng 0!");
            }

            doctorSchedule = DoctorSchedule.builder()
                    .id(UUID.randomUUID().toString())
                    .doctor(doctor)
                    .timeFrame(timeFrame)
                    .dayOfWeek(request.getDayOfWeek())
                    .maxPatients(request.getMaxPatients())
                    .status(true) // maxPatients > 0 nên status = true
                    .build();
        }

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

    public List<String> getListDayOfWeekByDoctor(String doctorId) {
        List<DayOfWeek> daysOfWeek = doctorScheduleRepository.findDistinctDaysOfWeekByDoctorId(doctorId);

        return daysOfWeek.stream()
                .map(DayOfWeek::name)
                .collect(Collectors.toList());
    }

    public List<String> getListDayOfWeekByDoctorService(String doctorServiceId) {
        List<DayOfWeek> daysOfWeek = doctorScheduleRepository.findDistinctDaysOfWeekByDoctorServiceId(doctorServiceId);

        return daysOfWeek.stream()
                .map(DayOfWeek::name)
                .collect(Collectors.toList());
    }

    // Lấy danh sách các lịch trình bác sĩ trong ngày với số lượng cuộc hẹn chưa đầy
    public ResponseEntity<List<DoctorScheduleResponse>> getScheduleByDoctorAndDate(String doctorId, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<DoctorSchedule> doctorSchedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
        List<DoctorScheduleResponse> response = new ArrayList<>();
        for (DoctorSchedule doctorSchedule : doctorSchedules) {
            long appointmentCount = appointmentRepository.countByDoctorScheduleIdAndAppointmentDate(doctorSchedule.getId(), date);

            if (appointmentCount < doctorSchedule.getMaxPatients()) {
                DoctorScheduleResponse scheduleResponse = doctorScheduleMapper.toDoctorScheduleResponse(doctorSchedule);
                response.add(scheduleResponse);
            }
        }
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<List<DoctorScheduleResponse>> createOrUpdateBatch(List<DoctorScheduleRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Danh sách lịch bác sĩ không được để trống!");
        }

        // Lấy danh sách doctorId và timeFrameId để kiểm tra tồn tại
        Set<String> doctorIds = requests.stream()
                .map(DoctorScheduleRequest::getDoctorId)
                .collect(Collectors.toSet());
        Set<String> timeFrameIds = requests.stream()
                .map(DoctorScheduleRequest::getTimeFrameId)
                .collect(Collectors.toSet());

        // Kiểm tra tồn tại của doctors và timeframes
        Map<String, Doctor> doctorMap = doctorRepository.findAllById(doctorIds).stream()
                .collect(Collectors.toMap(Doctor::getId, doctor -> doctor));
        Map<String, TimeFrame> timeFrameMap = timeFrameRepository.findAllById(timeFrameIds).stream()
                .collect(Collectors.toMap(TimeFrame::getId, timeFrame -> timeFrame));

        // Validate doctors và timeframes
        for (String doctorId : doctorIds) {
            if (!doctorMap.containsKey(doctorId)) {
                throw new IllegalArgumentException("Không tìm thấy bác sĩ có mã: " + doctorId);
            }
        }
        for (String timeFrameId : timeFrameIds) {
            if (!timeFrameMap.containsKey(timeFrameId)) {
                throw new IllegalArgumentException("Không tìm thấy khung giờ có mã: " + timeFrameId);
            }
        }

        List<DoctorSchedule> schedulesToSave = new ArrayList<>();

        for (DoctorScheduleRequest request : requests) {
            Doctor doctor = doctorMap.get(request.getDoctorId());
            TimeFrame timeFrame = timeFrameMap.get(request.getTimeFrameId());

            // Tìm lịch hiện có với doctor, timeframe và dayOfWeek
            Optional<DoctorSchedule> existingSchedule = doctorScheduleRepository.findByDoctorAndTimeFrameAndDayOfWeek(
                    doctor, timeFrame, request.getDayOfWeek());

            if (existingSchedule.isPresent()) {
                // Cập nhật lịch hiện có
                DoctorSchedule scheduleToUpdate = existingSchedule.get();
                scheduleToUpdate.setMaxPatients(request.getMaxPatients());
                scheduleToUpdate.setStatus(request.getStatus());
                schedulesToSave.add(scheduleToUpdate);
            } else if (request.getMaxPatients() > 0) {
                // Chỉ tạo mới nếu maxPatients > 0
                DoctorSchedule newSchedule = DoctorSchedule.builder()
                        .id(UUID.randomUUID().toString())
                        .doctor(doctor)
                        .timeFrame(timeFrame)
                        .dayOfWeek(request.getDayOfWeek())
                        .maxPatients(request.getMaxPatients())
                        .status(true) // maxPatients > 0 nên status = true
                        .build();
                schedulesToSave.add(newSchedule);
            }
        }

        // Lưu tất cả các lịch vào database trong một lần
        List<DoctorSchedule> savedSchedules = doctorScheduleRepository.saveAll(schedulesToSave);

        // Chuyển đổi kết quả thành response
        List<DoctorScheduleResponse> responses = savedSchedules.stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<List<DoctorScheduleResponse>> getActiveSchedules(String doctorId, DayOfWeek dayOfWeek) {
        if (doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bác sĩ không được để trống!");
        }
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Ngày trong tuần không được để trống!");
        }

        // Kiểm tra sự tồn tại của doctor
        if (!doctorRepository.existsById(doctorId)) {
            throw new IllegalArgumentException("Không tìm thấy bác sĩ với mã: " + doctorId);
        }

        List<DoctorSchedule> activeSchedules = doctorScheduleRepository
                .findActiveSchedulesByDoctorAndDayOfWeek(doctorId, dayOfWeek);

        List<DoctorScheduleResponse> responses = activeSchedules.stream()
                .map(doctorScheduleMapper::toDoctorScheduleResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<List<DoctorScheduleStatusResponse>> getStatuses() {
        List<DoctorScheduleStatusResponse> statuses = Arrays.stream(DoctorScheduleStatus.values())
                .map(status -> new DoctorScheduleStatusResponse(
                        status == DoctorScheduleStatus.ACTIVE,
                        status.getDescription()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(statuses);
    }
}