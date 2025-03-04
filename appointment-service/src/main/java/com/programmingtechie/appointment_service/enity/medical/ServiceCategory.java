package com.programmingtechie.appointment_service.enity.medical;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "service_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCategory {

    @Id
    @Column(nullable = false, length = 36, unique = true)
    private String id;

    @Column(name = "category_name", columnDefinition = "TEXT", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "serviceCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Service> services;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
