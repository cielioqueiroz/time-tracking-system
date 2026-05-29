package com.company.timetracking.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

import com.company.timetracking.domain.enums.CollaboratorStatus;

/**
 * JPA persistence model for a collaborator. Intentionally separate from the
 * domain {@code Collaborator} so persistence concerns never leak into the core.
 */
@Entity
@Table(name = "collaborators")
public class CollaboratorJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private CollaboratorStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CollaboratorJpaEntity() {
        // required by JPA
    }

    public CollaboratorJpaEntity(UUID id, String name, String email, String cargo,
                                 CollaboratorStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cargo = cargo;
        this.status = status;
    }

    @jakarta.persistence.PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @jakarta.persistence.PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public CollaboratorStatus getStatus() {
        return status;
    }

    public void setStatus(CollaboratorStatus status) {
        this.status = status;
    }
}
