package com.company.timetracking.domain.repositories;

import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.WorkSessionId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface WorkSessionRepository {

    WorkSession save(WorkSession session);

    Optional<WorkSession> findById(WorkSessionId id);

    Optional<WorkSession> findActiveByCollaborator(CollaboratorId collaboratorId);

    boolean existsActiveByCollaborator(CollaboratorId collaboratorId);

    Page<WorkSession> findByCollaborator(CollaboratorId collaboratorId, PageQuery query);

    List<WorkSession> findAllByCollaborator(CollaboratorId collaboratorId);

    WorkSessionSummary summaryByCollaborator(CollaboratorId collaboratorId, Instant from, Instant to);
}
