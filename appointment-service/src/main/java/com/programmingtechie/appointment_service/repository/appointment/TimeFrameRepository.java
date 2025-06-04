package com.programmingtechie.appointment_service.repository.appointment;

import java.time.LocalTime;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.appointment_service.enity.medical.TimeFrame;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

@Repository
public interface TimeFrameRepository extends JpaRepository<TimeFrame, String> {
    @Query("SELECT tf FROM TimeFrame tf WHERE " +
            "((tf.startTime <= :startTime AND :startTime < tf.endTime) OR " +
            "(tf.startTime < :endTime AND :endTime <= tf.endTime) OR " +
            "(:startTime <= tf.startTime AND tf.endTime <= :endTime)) " +
            "AND tf.status = :status")
    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.FETCH_SIZE, value = "1"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")
    })
    Optional<TimeFrame> findOverlappingTimeFrame(
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") Boolean status
    );

    @Query("SELECT tf FROM TimeFrame tf WHERE tf.status = :status " +
            "ORDER BY tf.startTime ASC")
    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.FETCH_SIZE, value = "50"),
            @QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")
    })
    List<TimeFrame> findAllActiveOrderBySessionAndTime(@Param("status") Boolean status);

    @Query("SELECT CASE WHEN COUNT(ds) > 0 THEN true ELSE false END " +
            "FROM DoctorSchedule ds WHERE ds.timeFrame.id = :timeFrameId")
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    boolean existsByDoctorSchedule(@Param("timeFrameId") String timeFrameId);

}