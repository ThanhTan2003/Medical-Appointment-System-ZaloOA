package com.programmingtechie.appointment_service.repository.doctor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.doctor.DoctorSchedule;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, String> {
    Page<DoctorSchedule> findByDoctorIdOrderByDayOfWeekAsc(String doctorId, Pageable pageable);

    Page<DoctorSchedule> findByTimeFrameIdOrderByDayOfWeekAsc(String timeFrameId, Pageable pageable);

    Page<DoctorSchedule> findByStatus(Boolean status, Pageable pageable);

    // Tìm danh sách các ngày trong tuần mà bác sĩ có lịch trình
    @Query("SELECT DISTINCT ds.dayOfWeek FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId")
    List<DayOfWeek> findDistinctDaysOfWeekByDoctorId(String doctorId);

    // Tìm danh sách các ngày trong tuần mà bác sĩ có lịch trình
    @Query("SELECT DISTINCT d.dayOfWeek " +
            "FROM DoctorSchedule d " +
            "JOIN DoctorService ds ON ds.doctor.id = d.doctor.id " +
            "WHERE ds.id = :doctorServiceId")
    List<DayOfWeek> findDistinctDaysOfWeekByDoctorServiceId(String doctorServiceId);

    // Tìm các lịch trình bác sĩ theo doctorId và dayOfWeek, sắp xếp theo thời gian bắt đầu của TimeFrame (startTime)
    @Query("SELECT ds FROM DoctorSchedule ds " +
            "JOIN ds.timeFrame tf " +
            "WHERE ds.doctor.id = :doctorId " +
            "AND ds.dayOfWeek = :dayOfWeek " +
            "ORDER BY tf.startTime ASC")
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(String doctorId, DayOfWeek dayOfWeek);
}
