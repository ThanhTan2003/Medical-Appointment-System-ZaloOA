package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.DoctorService;

@Repository
public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {
    Page<DoctorService> findByDoctorIdOrderByServiceFeeDesc(String doctorId, Pageable pageable);

    Page<DoctorService> findByServiceIdOrderByServiceFeeDesc(String serviceId, Pageable pageable);

    Page<DoctorService> findByStatus(Boolean status, Pageable pageable);
}
