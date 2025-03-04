package com.programmingtechie.appointment_service.repository.appointment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.appointment.Clinic;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {
    boolean existsBySupportPhone(String supportPhone);

    Optional<Clinic> findByClinicName(String clinicName);
}
