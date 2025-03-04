package com.programmingtechie.appointment_service.repository.vietnam_units;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.enity.vietnam_units.District;

public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceCode(String code);
}
