package com.programmingtechie.appointment_service.enity.notification;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "notification_receiver")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationReceiver {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @OneToMany(mappedBy = "notificationReceiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<NotificationPermission> notificationPermissions;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
