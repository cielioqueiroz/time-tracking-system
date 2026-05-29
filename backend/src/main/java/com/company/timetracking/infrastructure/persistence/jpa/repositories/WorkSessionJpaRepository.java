package com.company.timetracking.infrastructure.persistence.jpa.repositories;

import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.infrastructure.persistence.jpa.entities.WorkSessionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/** Spring Data repository for {@link WorkSessionJpaEntity}. */
public interface WorkSessionJpaRepository extends JpaRepository<WorkSessionJpaEntity, UUID> {

    Optional<WorkSessionJpaEntity> findFirstByCollaboratorIdAndStatus(
            UUID collaboratorId, WorkSessionStatus status);

    boolean existsByCollaboratorIdAndStatus(UUID collaboratorId, WorkSessionStatus status);

    Page<WorkSessionJpaEntity> findByCollaboratorId(UUID collaboratorId, Pageable pageable);
}
