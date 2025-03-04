package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.DoctorSchedule;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, String> {
    Page<DoctorSchedule> findByDoctorIdOrderByDayOfWeekAsc(String doctorId, Pageable pageable);

    Page<DoctorSchedule> findByTimeFrameIdOrderByDayOfWeekAsc(String timeFrameId, Pageable pageable);

    Page<DoctorSchedule> findByStatus(Boolean status, Pageable pageable);
}
