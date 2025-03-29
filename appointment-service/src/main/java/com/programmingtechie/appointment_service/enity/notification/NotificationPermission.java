package com.programmingtechie.appointment_service.enity.notification;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "notification_permissions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPermission {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_receiver_id", nullable = false)
    @ToString.Exclude
    private NotificationReceiver notificationReceiver;

    @Column(name = "permission", nullable = false)
    private String permission; // Quyền (ví dụ: RECEIVE_MESSAGE_NOTIFICATION, RECEIVE_APPOINTMENT_NOTIFICATION)

    @PrePersist
    private void ensurePermission() {
        if (this.id == null) this.id = UUID.randomUUID().toString();
    }
}
