package com.programmingtechie.appointment_service.repository.appointment;

import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

@Repository
public interface TimeFrameRepository extends JpaRepository<TimeFrame, String> {
    boolean existsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
    Page<TimeFrame> findByStatus(Boolean status, Pageable pageable);
}
