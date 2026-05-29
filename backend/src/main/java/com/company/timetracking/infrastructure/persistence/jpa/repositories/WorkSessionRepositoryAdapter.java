package com.company.timetracking.infrastructure.persistence.jpa.repositories;

import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.enums.WorkSessionStatus;
import com.company.timetracking.domain.repositories.Page;
import com.company.timetracking.domain.repositories.PageQuery;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.repositories.WorkSessionSummary;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.WorkSessionId;
import com.company.timetracking.infrastructure.persistence.jpa.entities.WorkSessionJpaEntity;
import com.company.timetracking.infrastructure.persistence.jpa.mappers.WorkSessionJpaMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** Outbound adapter implementing the {@link WorkSessionRepository} port. */
@Component
public class WorkSessionRepositoryAdapter implements WorkSessionRepository {

    private final WorkSessionJpaRepository jpa;
    private final WorkSessionJpaMapper mapper;

    public WorkSessionRepositoryAdapter(WorkSessionJpaRepository jpa,
                                        WorkSessionJpaMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public WorkSession save(WorkSession session) {
        WorkSessionJpaEntity entity = jpa.findById(session.id().value())
                .orElseGet(() -> mapper.toJpa(session));
        entity.setStatus(session.status());
        entity.setEndedAt(session.endedAt());
        entity.setTotalMinutes(session.totalMinutes());
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<WorkSession> findById(WorkSessionId id) {
        return jpa.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<WorkSession> findActiveByCollaborator(CollaboratorId collaboratorId) {
        return jpa.findFirstByCollaboratorIdAndStatus(
                        collaboratorId.value(), WorkSessionStatus.EM_ANDAMENTO)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsActiveByCollaborator(CollaboratorId collaboratorId) {
        return jpa.existsByCollaboratorIdAndStatus(
                collaboratorId.value(), WorkSessionStatus.EM_ANDAMENTO);
    }

    @Override
    public Page<WorkSession> findByCollaborator(CollaboratorId collaboratorId, PageQuery query) {
        var pageable = PageRequest.of(query.page(), query.size(),
                Sort.by("startedAt").descending());
        var result = jpa.findByCollaboratorId(collaboratorId.value(), pageable);
        return new Page<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    @Override
    public List<WorkSession> findAllByCollaborator(CollaboratorId collaboratorId) {
        return jpa.findByCollaboratorIdOrderByStartedAtDesc(collaboratorId.value())
                .stream().map(mapper::toDomain).toList();
    }

    // Concrete bounds for an "open" period: no work session predates the epoch,
    // and none reaches the year 9999 — so these act as "no lower/upper limit".
    private static final Instant MIN_BOUND = Instant.EPOCH;
    private static final Instant MAX_BOUND = Instant.parse("9999-12-31T23:59:59Z");

    @Override
    public WorkSessionSummary summaryByCollaborator(CollaboratorId collaboratorId, Instant from, Instant to) {
        var projection = jpa.summarizeByCollaborator(
                collaboratorId.value(),
                from != null ? from : MIN_BOUND,
                to != null ? to : MAX_BOUND);
        return new WorkSessionSummary(
                projection.getTotalSessions(),
                projection.getFinishedSessions(),
                projection.getTotalMinutes()
        );
    }
}
