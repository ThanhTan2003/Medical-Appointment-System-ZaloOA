package com.programmingtechie.appointment_service.utils;

import com.programmingtechie.appointment_service.repository.appointment.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientUtil {
    final PatientRepository patientRepository;

    public String generateId() {
        String prefix = "HS-";
        Random random = new Random();
        StringBuilder middlePart = new StringBuilder();
        StringBuilder suffixPart = new StringBuilder();

        String characters = "06BDYZVR2XJAW5KLTQSI9MC8UHE1OFG34NP7";
        for (int i = 0; i < 15; i++) {
            suffixPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        String doctorId = prefix + middlePart.toString() + suffixPart.toString();

        boolean exists = patientRepository.existsById(doctorId);
        if (!exists) {
            return doctorId;
        }

        return generateId();
    }
}
