package com.programmingtechie.appointment_service.enity.medical;

import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "time_frame")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeFrame {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "session", nullable = false)
    private String session;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
