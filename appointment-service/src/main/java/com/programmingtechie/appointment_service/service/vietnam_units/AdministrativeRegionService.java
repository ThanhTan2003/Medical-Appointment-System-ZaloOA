package com.programmingtechie.appointment_service.service.vietnam_units;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.enity.vietnam_units.AdministrativeRegion;
import com.programmingtechie.appointment_service.repository.vietnam_units.AdministrativeRegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdministrativeRegionService {
    final AdministrativeRegionRepository administrativeRegionRepository;

    public void createRegion(AdministrativeRegion region) {
        administrativeRegionRepository.save(region);
    }

    public List<AdministrativeRegion> getAllRegions() {
        return administrativeRegionRepository.findAll();
    }
}
