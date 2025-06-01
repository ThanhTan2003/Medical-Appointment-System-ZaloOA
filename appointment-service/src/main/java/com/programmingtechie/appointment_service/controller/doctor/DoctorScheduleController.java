package com.programmingtechie.appointment_service.controller.doctor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorScheduleRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorScheduleResponse;
import com.programmingtechie.appointment_service.service.doctor.DoctorScheduleService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment/doctor-schedule")
@RequiredArgsConstructor
public class DoctorScheduleController {
    final DoctorScheduleService doctorScheduleService;

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorScheduleResponse> create(@RequestBody DoctorScheduleRequest request) {
        return doctorScheduleService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorScheduleResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorScheduleService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorScheduleResponse> getById(@PathVariable String id) {
        return doctorScheduleService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorScheduleResponse> updateById(
            @PathVariable String id, @RequestBody DoctorScheduleRequest request) {
        return doctorScheduleService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return doctorScheduleService.deleteById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public PageResponse<DoctorScheduleResponse> getByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorScheduleService.getByDoctorId(doctorId, page, size);
    }

    @GetMapping("/doctor/{doctorId}/schedule")
    public ResponseEntity<List<DoctorScheduleResponse>> getScheduleByDoctorAndDate(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return doctorScheduleService.getScheduleByDoctorAndDate(doctorId, date);
    }


    @GetMapping("/timeframe/{timeFrameId}")
    public PageResponse<DoctorScheduleResponse> getByTimeFrameId(
            @PathVariable String timeFrameId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorScheduleService.getByTimeFrameId(timeFrameId, page, size);
    }

    @GetMapping("/status/{status}")
    public PageResponse<DoctorScheduleResponse> getByStatus(
            @PathVariable Boolean status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorScheduleService.getByStatus(status, page, size);
    }

    @GetMapping("/get-day-of-week-by-doctor/{doctorId}")
    public ResponseEntity<List<String>> getListDayOfWeekByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(doctorScheduleService.getListDayOfWeekByDoctor(doctorId));
    }

    @GetMapping("/get-day-of-week-by-doctor-service/{doctorServiceId}")
    public ResponseEntity<List<String>> getListDayOfWeekByDoctorService(@PathVariable String doctorServiceId) {
        return ResponseEntity.ok(doctorScheduleService.getListDayOfWeekByDoctorService(doctorServiceId));
    }
}
