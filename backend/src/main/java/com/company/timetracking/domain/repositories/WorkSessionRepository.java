package com.company.timetracking.domain.repositories;

import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.WorkSessionId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Outbound port for work-session persistence. Implemented by an adapter in the
 * infrastructure layer.
 */
public interface WorkSessionRepository {

    WorkSession save(WorkSession session);

    Optional<WorkSession> findById(WorkSessionId id);

    /** The single open session of a collaborator, if any. */
    Optional<WorkSession> findActiveByCollaborator(CollaboratorId collaboratorId);

    boolean existsActiveByCollaborator(CollaboratorId collaboratorId);

    /** Session history of a collaborator, newest first. */
    Page<WorkSession> findByCollaborator(CollaboratorId collaboratorId, PageQuery query);

    /** All sessions of a collaborator, newest first (for export). */
    List<WorkSession> findAllByCollaborator(CollaboratorId collaboratorId);

    /**
     * Aggregated statistics for a collaborator. {@code from}/{@code to} (on
     * {@code startedAt}) are optional bounds — pass {@code null} to leave a side open.
     */
    WorkSessionSummary summaryByCollaborator(CollaboratorId collaboratorId, Instant from, Instant to);
}
