package com.programmingtechie.zalo_oa_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class Token {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String accessToken;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String refreshToken;

    private LocalDateTime expires;
}
