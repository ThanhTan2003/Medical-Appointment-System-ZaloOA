package com.programmingtechie.appointment_service.controller.doctor;

import com.programmingtechie.appointment_service.dto.response.doctor.DoctorResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceStatusResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorServiceRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;
import com.programmingtechie.appointment_service.service.doctor.DoctorServiceService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment/doctor-service")
@RequiredArgsConstructor
public class DoctorServiceController {
    final DoctorServiceService doctorServiceService;

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorServiceResponse> create(@RequestBody DoctorServiceRequest request) {
        return doctorServiceService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getAll(page, size);
    }

    @GetMapping("/search-by-service")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> searchByService(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String serviceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.searchByService(keyword, serviceId, page, size);
    }

    @GetMapping("/search-by-doctor")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> searchByDoctor(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String doctorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.searchByDoctor(keyword, doctorId, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorServiceResponse> getById(@PathVariable String id) {
        return doctorServiceService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<DoctorServiceResponse> updateById(
            @PathVariable String id, @RequestBody DoctorServiceRequest request) {
        return doctorServiceService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return doctorServiceService.deleteById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> getByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByDoctorId(doctorId, page, size);
    }

    @GetMapping("/service/{serviceId}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> getByServiceId(
            @PathVariable String serviceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByServiceId(serviceId, page, size);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> getByStatus(
            @PathVariable Boolean status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByStatus(status, page, size);
    }

    @GetMapping("/statuses")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<List<DoctorServiceStatusResponse>> getAllDoctorServiceStatuses() {
        return ResponseEntity.ok(doctorServiceService.getAllDoctorServiceStatuses());
    }

    @GetMapping("/search-by-doctor-and-category")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public PageResponse<DoctorServiceResponse> searchByDoctorAndServiceCategory(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam String doctorId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String serviceCategoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return doctorServiceService.searchByDoctorWithFilters(keyword, doctorId, status, serviceCategoryId, page, size);
    }
}
