package com.programmingtechie.appointment_service.controller.doctor;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public PageResponse<DoctorResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorService.getAll(page, size);
    }

    @GetMapping("/search-by-service")
    public PageResponse<DoctorResponse> searchByService(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String serviceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorService.searchByService(keyword, serviceId, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(@PathVariable String id) {
        return doctorService.getById(id);
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest doctorRequest) {
        return doctorService.create(doctorRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(@PathVariable String id, @RequestBody DoctorRequest doctorRequest) {
        return doctorService.updateById(id, doctorRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return doctorService.deleteById(id);
    }

    @GetMapping("/search/status")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctorsByStatus(
            @RequestParam Boolean status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        PageResponse<DoctorResponse> response = doctorService.searchDoctorsByStatus(status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctors(
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageResponse<DoctorResponse> response = doctorService.searchDoctors(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
