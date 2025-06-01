package com.programmingtechie.appointment_service.controller.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.PatientRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.appointment.PatientResponse;
import com.programmingtechie.appointment_service.service.appointment.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/patient")
@RequiredArgsConstructor
public class PatientController {
    final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<PatientResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientService.getAll(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PatientResponse> getById(@PathVariable String id) {
        return patientService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest patientRequest) {
        return patientService.create(patientRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PatientResponse> updateById(
            @PathVariable String id, @RequestBody PatientRequest patientRequest) {
        return patientService.updateById(id, patientRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return patientService.deleteById(id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<PatientResponse>> searchPatients(
            @RequestParam(required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<PatientResponse> response = patientService.getPatients(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
