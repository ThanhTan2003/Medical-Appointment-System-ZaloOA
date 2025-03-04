package com.programmingtechie.appointment_service.controller.doctor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.doctor.DoctorServiceRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.doctor.DoctorServiceResponse;
import com.programmingtechie.appointment_service.service.doctor.DoctorServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/doctor-services")
@RequiredArgsConstructor
public class DoctorServiceController {
    final DoctorServiceService doctorServiceService;

    @PostMapping
    public ResponseEntity<DoctorServiceResponse> create(@RequestBody DoctorServiceRequest request) {
        return doctorServiceService.create(request);
    }

    @GetMapping
    public PageResponse<DoctorServiceResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorServiceResponse> getById(@PathVariable String id) {
        return doctorServiceService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorServiceResponse> updateById(
            @PathVariable String id, @RequestBody DoctorServiceRequest request) {
        return doctorServiceService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        return doctorServiceService.deleteById(id);
    }

    @GetMapping("/doctor/{doctorId}")
    public PageResponse<DoctorServiceResponse> getByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByDoctorId(doctorId, page, size);
    }

    @GetMapping("/service/{serviceId}")
    public PageResponse<DoctorServiceResponse> getByServiceId(
            @PathVariable String serviceId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByServiceId(serviceId, page, size);
    }

    @GetMapping("/status/{status}")
    public PageResponse<DoctorServiceResponse> getByStatus(
            @PathVariable Boolean status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceService.getByStatus(status, page, size);
    }
}
