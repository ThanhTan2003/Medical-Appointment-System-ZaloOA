package com.programmingtechie.zalo_oa_service.entity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "zalo_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZaloUser {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "display_name", columnDefinition = "TEXT")
    private String displayName;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
