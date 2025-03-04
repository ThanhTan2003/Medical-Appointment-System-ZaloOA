package com.programmingtechie.appointment_service.controller.appointment;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.AppointmentResponse;
import com.programmingtechie.appointment_service.service.appointment.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody AppointmentRequest request) {
        return appointmentService.create(request);
    }

    @GetMapping("/doctor/{doctorServiceId}")
    public PageResponse<AppointmentResponse> getByDoctorServiceId(
            @PathVariable String doctorServiceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return appointmentService.getByDoctorServiceId(doctorServiceId, page, size);
    }

    @GetMapping("/zalo/{zaloUid}")
    public PageResponse<AppointmentResponse> getByZaloUid(
            @PathVariable String zaloUid,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return appointmentService.getByZaloUid(zaloUid, page, size);
    }

    @GetMapping("/patient/{patientId}")
    public PageResponse<AppointmentResponse> getByPatientId(
            @PathVariable String patientId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return appointmentService.getByPatientId(patientId, page, size);
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
}
