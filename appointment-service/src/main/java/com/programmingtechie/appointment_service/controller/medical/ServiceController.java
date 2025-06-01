package com.programmingtechie.appointment_service.controller.medical;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.medical.ServiceRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.medical.ServiceResponse;
import com.programmingtechie.appointment_service.service.medical.ServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/service")
@RequiredArgsConstructor
public class ServiceController {
    final ServiceService serviceService;

    @GetMapping
    public PageResponse<ServiceResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return serviceService.getAll(page, size);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<ServiceResponse> search(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") String serviceCategoryId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return serviceService.search(keyword, serviceCategoryId, page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceResponse> getById(@PathVariable String id) {
        return serviceService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@RequestBody ServiceRequest serviceRequest) {
        return serviceService.create(serviceRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceResponse> updateById(
            @PathVariable String id, @RequestBody ServiceRequest serviceRequest) {
        return serviceService.updateById(id, serviceRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return serviceService.deleteById(id);
    }

    @GetMapping("/search-not-registered-by-doctor")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public PageResponse<ServiceResponse> searchServicesNotRegisteredByDoctor(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam String doctorId,
            @RequestParam(required = false) String serviceCategoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return serviceService.searchServicesNotRegisteredByDoctor(keyword, doctorId, serviceCategoryId, page, size);
    }
}
