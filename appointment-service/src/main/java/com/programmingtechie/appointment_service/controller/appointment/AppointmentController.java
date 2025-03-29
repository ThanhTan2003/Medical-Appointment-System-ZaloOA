package com.programmingtechie.appointment_service.controller.appointment;

import java.time.LocalDate;
import java.util.List;

import com.programmingtechie.appointment_service.enity.appointment.Appointment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.AppointmentResponse;
import com.programmingtechie.appointment_service.service.appointment.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/main")
@RequiredArgsConstructor
public class AppointmentController {
    final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById( @PathVariable String id) {
        AppointmentResponse response = appointmentService.getById(id).getBody();

        return ResponseEntity.ok(response);
    }

    // API tìm kiếm các cuộc hẹn theo doctorServiceId và status (nếu có)
    @GetMapping("/doctor/{doctorServiceId}")
    public ResponseEntity<PageResponse<AppointmentResponse>> getByDoctorServiceId(
            @PathVariable String doctorServiceId,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<AppointmentResponse> response =
                appointmentService.getByDoctorServiceId(doctorServiceId, status, page, size);

        return ResponseEntity.ok(response);
    }

    // API tìm kiếm các cuộc hẹn theo zaloUid và status (nếu có)
    @GetMapping("/zalo/{zaloUid}")
    public ResponseEntity<PageResponse<AppointmentResponse>> getByZaloUid(
            @PathVariable String zaloUid,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<AppointmentResponse> response = appointmentService.getByZaloUid(zaloUid, status, page, size);

        return ResponseEntity.ok(response);
    }

    // API tìm kiếm các cuộc hẹn theo patientId và status (nếu có)
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PageResponse<AppointmentResponse>> getByPatientId(
            @PathVariable String patientId,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<AppointmentResponse> response = appointmentService.getByPatientId(patientId, status, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date/{appointmentDate}")
    public PageResponse<AppointmentResponse> getByAppointmentDate(
            @PathVariable LocalDate appointmentDate,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return appointmentService.getByAppointmentDate(appointmentDate, page, size);
    }

    // API trả về danh sách các trạng thái cuộc hẹn
    @GetMapping("/statuses")
    public ResponseEntity<String[]> getAppointmentStatuses() {
        String[] statuses = appointmentService.getAllAppointmentStatuses();
        return ResponseEntity.ok(statuses);
    }

    // API tìm kiếm các cuộc hẹn theo doctorId, appointmentDate và status (nếu có)
    @GetMapping("/search")
    public ResponseEntity<PageResponse<AppointmentResponse>> getAppointmentsByFilters(
            @RequestParam String doctorId,
            @RequestParam LocalDate appointmentDate,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<AppointmentResponse> response =
                appointmentService.getAppointmentsByFilters(doctorId, appointmentDate, status, page, size);

        return ResponseEntity.ok(response);
    }

    // API xác nhận lịch hẹn (Cập nhật status thành "Đã phê duyệt")
    @PutMapping("/confirm/{appointmentId}")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable String appointmentId) {
        return appointmentService.confirmAppointment(appointmentId);
    }

    // API huỷ lịch hẹn (Cập nhật status thành "Đã huỷ")
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable String appointmentId) {
        return appointmentService.cancelAppointment(appointmentId);
    }

    // API nhắc lịch khám (Cập nhật status thành "Chờ khám")
    @PutMapping("/remind/{appointmentId}")
    public ResponseEntity<AppointmentResponse> remindAppointment(@PathVariable String appointmentId) {
        return appointmentService.remindAppointment(appointmentId);
    }

    // API đánh dấu là đã khám (Cập nhật status thành "Đã khám")
    @PutMapping("/mark-as-examined/{appointmentId}")
    public ResponseEntity<AppointmentResponse> markAsExamined(@PathVariable String appointmentId) {
        return appointmentService.markAsExamined(appointmentId);
    }

    // API để tìm danh sách các cuộc hẹn của bác sĩ theo lịch trình và ngày hẹn
    @GetMapping("/search-by-schedule")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByScheduleAndDate(
            @RequestParam String doctorScheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByScheduleAndDate(doctorScheduleId, appointmentDate);
        return ResponseEntity.ok(appointments);
    }
}
