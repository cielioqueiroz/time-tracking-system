package com.company.timetracking.infrastructure.persistence.jpa.entities;

import com.company.timetracking.domain.enums.WorkSessionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * JPA persistence model for a work session.
 */
@Entity
@Table(name = "work_sessions", indexes = {
        @Index(name = "ix_work_sessions_collaborator_started",
                columnList = "collaborator_id, started_at")
})
public class WorkSessionJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "collaborator_id", nullable = false)
    private UUID collaboratorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private WorkSessionStatus status;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "ended_at")
    private Instant endedAt;

    @Column(name = "total_minutes")
    private Long totalMinutes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected WorkSessionJpaEntity() {
        // required by JPA
    }

    public WorkSessionJpaEntity(UUID id, UUID collaboratorId, WorkSessionStatus status,
                                Instant startedAt, Instant endedAt, Long totalMinutes) {
        this.id = id;
        this.collaboratorId = collaboratorId;
        this.status = status;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.totalMinutes = totalMinutes;
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

    public UUID getCollaboratorId() {
        return collaboratorId;
    }

    public WorkSessionStatus getStatus() {
        return status;
    }

    public void setStatus(WorkSessionStatus status) {
        this.status = status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

    public Long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }
}
