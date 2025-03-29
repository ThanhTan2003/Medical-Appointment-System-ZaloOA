package com.programmingtechie.zalo_oa_service.entity;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "zalo_user_tag")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZaloUserTag {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zalo_user_id", nullable = false)
    @ToString.Exclude
    private ZaloUser zaloUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    @ToString.Exclude
    private Tag tag;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
