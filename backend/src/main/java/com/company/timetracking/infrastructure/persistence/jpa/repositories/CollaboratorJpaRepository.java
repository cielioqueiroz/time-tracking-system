package com.company.timetracking.infrastructure.persistence.jpa.repositories;

import com.company.timetracking.infrastructure.persistence.jpa.entities.CollaboratorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/** Spring Data repository for {@link CollaboratorJpaEntity}. */
public interface CollaboratorJpaRepository extends JpaRepository<CollaboratorJpaEntity, UUID> {

    Optional<CollaboratorJpaEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);
}
