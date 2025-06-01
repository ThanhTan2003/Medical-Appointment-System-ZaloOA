package com.programmingtechie.appointment_service.controller.medical;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.medical.ServiceCategoryRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.medical.ServiceCategoryResponse;
import com.programmingtechie.appointment_service.service.medical.ServiceCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/service-category")
@RequiredArgsConstructor
public class ServiceCategoryController {
    final ServiceCategoryService serviceCategoryService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public PageResponse<ServiceCategoryResponse> search(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return serviceCategoryService.search(page, size, keyword);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceCategoryResponse> getById(@PathVariable String id) {
        return serviceCategoryService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceCategoryResponse> create(@RequestBody ServiceCategoryRequest serviceCategoryRequest) {
        return serviceCategoryService.create(serviceCategoryRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceCategoryResponse> updateById(
            @PathVariable String id, @RequestBody ServiceCategoryRequest serviceCategoryRequest) {
        return serviceCategoryService.updateById(id, serviceCategoryRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return serviceCategoryService.deleteById(id);
    }


    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public PageResponse<ServiceCategoryResponse> getByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return serviceCategoryService.getByDoctorId(doctorId, page, size);
    }
}
