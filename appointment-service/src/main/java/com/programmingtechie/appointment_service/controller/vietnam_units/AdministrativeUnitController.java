package com.programmingtechie.appointment_service.controller.vietnam_units;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.enity.vietnam_units.AdministrativeUnit;
import com.programmingtechie.appointment_service.service.vietnam_units.AdministrativeUnitService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/patient/vietnam-units/administrative-unit")
@RequiredArgsConstructor
@Slf4j
public class AdministrativeUnitController {
    final AdministrativeUnitService administrativeUnitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUnit(@RequestBody AdministrativeUnit unit) {
        administrativeUnitService.createUnit(unit);
        return "Created new administrative unit successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdministrativeUnit> getAllUnits() {
        return administrativeUnitService.getAllUnits();
    }
}
