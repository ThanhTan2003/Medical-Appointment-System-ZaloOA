package com.programmingtechie.appointment_service.controller.vietnam_units;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.enity.vietnam_units.AdministrativeRegion;
import com.programmingtechie.appointment_service.service.vietnam_units.AdministrativeRegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/patient/vietnam-units/administrative-region")
@RequiredArgsConstructor
@Slf4j
public class AdministrativeRegionController {
    final AdministrativeRegionService administrativeRegionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createRegion(@RequestBody AdministrativeRegion region) {
        administrativeRegionService.createRegion(region);
        return "Created new administrative region successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdministrativeRegion> getAllRegions() {
        return administrativeRegionService.getAllRegions();
    }
}
