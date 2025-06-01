package com.programmingtechie.identity_service.model;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_type_id", nullable = false)
    private Role role; // Liên kết với bảng `AccountType`

    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "zalo_uid")
    private String zaloUID;

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
