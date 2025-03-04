package com.programmingtechie.appointment_service.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.appointment.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByIdentityCard(String identityCard);
}
