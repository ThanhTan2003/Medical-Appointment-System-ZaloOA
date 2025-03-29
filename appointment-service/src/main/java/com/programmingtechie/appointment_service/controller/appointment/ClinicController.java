package com.programmingtechie.appointment_service.controller.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.appointment.ClinicRequest;
import com.programmingtechie.appointment_service.dto.response.appointment.ClinicResponse;
import com.programmingtechie.appointment_service.service.appointment.ClinicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/clinic")
@RequiredArgsConstructor
public class ClinicController {
    final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<ClinicResponse> getClinicInfo() {
        return clinicService.getClinicInfo();
    }

    @PostMapping
    public ResponseEntity<ClinicResponse> createOrUpdateClinic(@RequestBody ClinicRequest clinicRequest) {
        return clinicService.createOrUpdateClinic(clinicRequest);
    }
}
