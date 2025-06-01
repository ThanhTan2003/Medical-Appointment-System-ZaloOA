package com.programmingtechie.appointment_service.controller.doctor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorStatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorResponse;
import com.programmingtechie.appointment_service.service.doctor.DoctorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/doctor")
@RequiredArgsConstructor
public class DoctorController {
    final DoctorService doctorService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorService.getAll(page, size);
    }

    @GetMapping("/search-by-service")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorResponse> searchByService(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String serviceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorService.searchByService(keyword, serviceId, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorResponse> getById(@PathVariable String id) {
        return doctorService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest doctorRequest) {
        return doctorService.create(doctorRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorResponse> update(@PathVariable String id, @RequestBody DoctorRequest doctorRequest) {
        return doctorService.updateById(id, doctorRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return doctorService.deleteById(id);
    }

    @GetMapping("/search/status")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctorsByStatus(
            @RequestParam Boolean status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<DoctorResponse> response = doctorService.searchDoctorsByStatus(status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctors(
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<DoctorResponse> response = doctorService.searchDoctors(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/keyword-status-category")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctorsWithStatusAndCategory(
            @RequestParam String keyword,
            @RequestParam Boolean status,
            @RequestParam String serviceCategoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<DoctorResponse> response = doctorService.searchDoctorsWithStatusAndCategory(
                keyword,
                status,
                serviceCategoryId,
                page,
                size
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<List<DoctorStatusResponse>> getAllDoctorStatuses() {
        return ResponseEntity.ok(doctorService.getAllDoctorStatuses());
    }
}