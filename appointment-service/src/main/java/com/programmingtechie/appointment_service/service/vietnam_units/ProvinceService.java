package com.programmingtechie.appointment_service.service.vietnam_units;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.enity.vietnam_units.Province;
import com.programmingtechie.appointment_service.repository.vietnam_units.ProvinceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProvinceService {
    final ProvinceRepository provinceRepository;

    public void createProvince(Province province) {
        provinceRepository.save(province);
    }

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}
