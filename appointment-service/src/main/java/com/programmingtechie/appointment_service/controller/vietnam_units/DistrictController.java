package com.programmingtechie.appointment_service.controller.vietnam_units;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.enity.vietnam_units.District;
import com.programmingtechie.appointment_service.service.vietnam_units.DistrictService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/patient/vietnam-units/district")
@RequiredArgsConstructor
@Slf4j
public class DistrictController {
    final DistrictService districtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createDistrict(@RequestBody District district) {
        districtService.createDistrict(district);
        return "Created new district successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/province-code/{code}")
    public List<District> getByProvince_Code(@PathVariable String code) {
        return districtService.getByProvince_Code(code);
    }
}
