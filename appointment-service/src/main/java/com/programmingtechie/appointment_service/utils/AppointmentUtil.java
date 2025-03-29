package com.programmingtechie.appointment_service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.repository.appointment.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentUtil {
    final AppointmentRepository appointmentRepository;

    public String generateAppointmentId() {
        String prefix = "LH-";

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        String datePart = sdf.format(new Date());

        StringBuilder suffixPart = new StringBuilder();
        String characters = "06BDYZVR2XJAW5KLTQSI9MC8UHE1OFG34NP7";
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            suffixPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        String doctorId = prefix + datePart + suffixPart.toString();

        boolean exists = appointmentRepository.existsById(doctorId);
        if (!exists) {
            return doctorId;
        }

        return generateAppointmentId();
    }
}