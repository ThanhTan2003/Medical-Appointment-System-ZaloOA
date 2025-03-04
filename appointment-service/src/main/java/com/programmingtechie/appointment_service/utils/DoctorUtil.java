package com.programmingtechie.appointment_service.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.repository.doctor.DoctorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorUtil {
    final DoctorRepository doctorRepository;

    public String generateDoctorId() {
        String prefix = "BS-";
        Random random = new Random();
        StringBuilder middlePart = new StringBuilder();
        StringBuilder suffixPart = new StringBuilder();

        String characters = "06BDYZVR2XJAW5KLTQSI9MC8UHE1OFG34NP7";
        for (int i = 0; i < 7; i++) {
            suffixPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        String doctorId = prefix + middlePart.toString() + suffixPart.toString();

        boolean exists = doctorRepository.existsById(doctorId);
        if (!exists) {
            return doctorId;
        }

        return generateDoctorId();
    }
}
