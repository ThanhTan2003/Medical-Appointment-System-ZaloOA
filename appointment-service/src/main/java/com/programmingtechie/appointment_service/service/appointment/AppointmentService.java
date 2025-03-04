package com.programmingtechie.appointment_service.service.appointment;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.appointment.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.AppointmentResponse;
import com.programmingtechie.appointment_service.enity.appointment.Appointment;
import com.programmingtechie.appointment_service.enity.appointment.Patient;
import com.programmingtechie.appointment_service.enity.doctor.DoctorService;
import com.programmingtechie.appointment_service.enity.medical.TimeFrame;
import com.programmingtechie.appointment_service.enums.AppointmentStatus;
import com.programmingtechie.appointment_service.mapper.appointment.AppointmentMapper;
import com.programmingtechie.appointment_service.repository.appointment.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.appointment.PatientRepository;
import com.programmingtechie.appointment_service.repository.appointment.TimeFrameRepository;
import com.programmingtechie.appointment_service.repository.doctor.DoctorServiceRepository;
import com.programmingtechie.appointment_service.utils.AppointmentUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    final AppointmentRepository appointmentRepository;
    final PatientRepository patientRepository;
    final DoctorServiceRepository doctorServiceRepository;
    final TimeFrameRepository timeFrameRepository;
    final AppointmentMapper appointmentMapper;
    final AppointmentUtil appointmentUtil;

    private void validateAppointmentRequest(AppointmentRequest request) {
        if (request == null || request.getPatientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Bệnh nhân không được để trống!");
        }
        if (request.getDoctorServiceId() == null
                || request.getDoctorServiceId().trim().isEmpty()) {
            throw new IllegalArgumentException("Dịch vụ y tế không được để trống!");
        }
        if (request.getTimeFrameId() == null || request.getTimeFrameId().trim().isEmpty()) {
            throw new IllegalArgumentException("Khung giờ hẹn không được để trống!");
        }
        if (request.getAppointmentDate() == null) {
            throw new IllegalArgumentException("Ngày hẹn không được để trống!");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái lịch hẹn không được để trống!");
        }
    }

    public PageResponse<AppointmentResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData = appointmentRepository.findAll(pageable);

        List<AppointmentResponse> responses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responses)
                .build();
    }

    public ResponseEntity<AppointmentResponse> getById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch hẹn!");
        }

        return appointmentRepository
                .findById(id)
                .map(appointment -> ResponseEntity.ok(appointmentMapper.toAppointmentResponse(appointment)))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn có mã " + id + "!"));
    }

    public ResponseEntity<AppointmentResponse> create(AppointmentRequest request) {
        validateAppointmentRequest(request);

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân!"));

        DoctorService doctorService = doctorServiceRepository
                .findById(request.getDoctorServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ y tế!"));

        TimeFrame timeFrame = timeFrameRepository
                .findById(request.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ hẹn!"));

        Appointment appointment = Appointment.builder()
                .id(appointmentUtil.generateAppointmentId())
                .zaloUid(request.getZaloUid())
                .patient(patient)
                .doctorService(doctorService)
                .timeFrame(timeFrame)
                .bookingTime(request.getBookingTime())
                .appointmentDate(request.getAppointmentDate())
                .status(request.getStatus())
                .build();

        appointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(appointmentMapper.toAppointmentResponse(appointment));
    }

    public ResponseEntity<AppointmentResponse> updateById(String id, AppointmentRequest request) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch hẹn!");
        }
        validateAppointmentRequest(request);

        Appointment existingAppointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn có mã " + id + "!"));

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bệnh nhân!"));

        DoctorService doctorService = doctorServiceRepository
                .findById(request.getDoctorServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ y tế!"));

        TimeFrame timeFrame = timeFrameRepository
                .findById(request.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung giờ hẹn!"));

        existingAppointment.setZaloUid(request.getZaloUid());
        existingAppointment.setPatient(patient);
        existingAppointment.setDoctorService(doctorService);
        existingAppointment.setTimeFrame(timeFrame);
        existingAppointment.setBookingTime(request.getBookingTime());
        existingAppointment.setAppointmentDate(request.getAppointmentDate());
        existingAppointment.setStatus(request.getStatus());

        appointmentRepository.save(existingAppointment);
        return ResponseEntity.ok(appointmentMapper.toAppointmentResponse(existingAppointment));
    }

    public ResponseEntity<String> deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp mã lịch hẹn!");
        }

        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn có mã " + id + "!");
        }

        appointmentRepository.deleteById(id);
        return ResponseEntity.ok("Lịch hẹn có mã " + id + " đã được xóa thành công.");
    }

    public PageResponse<AppointmentResponse> getByDoctorServiceId(String doctorServiceId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData =
                appointmentRepository.findByDoctorServiceIdOrderByAppointmentDateDesc(doctorServiceId, pageable);

        List<AppointmentResponse> responses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<AppointmentResponse> getByZaloUid(String zaloUid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData = appointmentRepository.findByZaloUidOrderByAppointmentDateDesc(zaloUid, pageable);

        List<AppointmentResponse> responses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<AppointmentResponse> getByPatientId(String patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData =
                appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId, pageable);

        List<AppointmentResponse> responses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    public PageResponse<AppointmentResponse> getByAppointmentDate(LocalDate appointmentDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData =
                appointmentRepository.findByAppointmentDateOrderByAppointmentDateDesc(appointmentDate, pageable);

        List<AppointmentResponse> responses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(pageData.getTotalPages(), page, size, pageData.getTotalElements(), responses);
    }

    // API trả về danh sách trạng thái cuộc hẹn
    public String[] getAllAppointmentStatuses() {
        return Arrays.stream(AppointmentStatus.values())
                .map(AppointmentStatus::getDescription)
                .toArray(String[]::new);
    }

    // Tìm kiếm các cuộc hẹn theo doctorId, appointmentDate và status
    public PageResponse<AppointmentResponse> getAppointmentsByFilters(
            String doctorId, LocalDate appointmentDate, String status, int page, int size) {

        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Appointment> appointmentPage = appointmentRepository.findByDoctorIdAndAppointmentDateAndStatus(
                doctorId, appointmentDate, status, pageable);

        List<AppointmentResponse> appointmentResponses =
                appointmentMapper.toAppointmentResponseList(appointmentPage.getContent());

        return new PageResponse<>(
                appointmentPage.getTotalPages(), page, size, appointmentPage.getTotalElements(), appointmentResponses);
    }
}
