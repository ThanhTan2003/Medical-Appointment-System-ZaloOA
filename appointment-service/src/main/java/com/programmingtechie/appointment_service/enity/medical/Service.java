package com.programmingtechie.appointment_service.enity.medical;

import java.util.UUID;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Entity
@Table(name = "service")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "name", columnDefinition = "TEXT", nullable = false)
    private String serviceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private String price;

    @ManyToOne
    @JoinColumn(name = "service_category_id", nullable = false)
    @JsonIgnore
    private ServiceCategory serviceCategory;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
