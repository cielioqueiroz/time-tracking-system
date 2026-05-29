package com.company.timetracking.infrastructure.persistence.jpa.repositories;

import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.infrastructure.persistence.jpa.entities.WorkSessionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkSessionJpaRepository extends JpaRepository<WorkSessionJpaEntity, UUID> {

    Optional<WorkSessionJpaEntity> findFirstByCollaboratorIdAndStatus(
            UUID collaboratorId, WorkSessionStatus status);

    boolean existsByCollaboratorIdAndStatus(UUID collaboratorId, WorkSessionStatus status);

    Page<WorkSessionJpaEntity> findByCollaboratorId(UUID collaboratorId, Pageable pageable);

    List<WorkSessionJpaEntity> findByCollaboratorIdOrderByStartedAtDesc(UUID collaboratorId);

    interface SummaryProjection {
        long getTotalSessions();
        long getFinishedSessions();
        long getTotalMinutes();
    }

    @Query("""
            select count(w) as totalSessions,
                   coalesce(sum(case when w.status = com.company.timetracking.domain.enums.WorkSessionStatus.FINALIZADA
                                     then 1 else 0 end), 0) as finishedSessions,
                   coalesce(sum(w.totalMinutes), 0) as totalMinutes
            from WorkSessionJpaEntity w
            where w.collaboratorId = :collaboratorId
              and w.startedAt >= :from
              and w.startedAt <= :to
            """)
    SummaryProjection summarizeByCollaborator(@Param("collaboratorId") UUID collaboratorId,
                                              @Param("from") Instant from,
                                              @Param("to") Instant to);
}
