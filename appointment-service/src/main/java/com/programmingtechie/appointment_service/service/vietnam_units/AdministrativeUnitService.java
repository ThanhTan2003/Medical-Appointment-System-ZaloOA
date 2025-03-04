package com.programmingtechie.appointment_service.service.vietnam_units;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.enity.vietnam_units.AdministrativeUnit;
import com.programmingtechie.appointment_service.repository.vietnam_units.AdministrativeUnitRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdministrativeUnitService {
    final AdministrativeUnitRepository administrativeUnitRepository;

    public void createUnit(AdministrativeUnit unit) {
        administrativeUnitRepository.save(unit);
    }

    public List<AdministrativeUnit> getAllUnits() {
        return administrativeUnitRepository.findAll();
    }
}
