package com.company.timetracking.infrastructure.persistence.jpa.mappers;

import com.company.timetracking.domain.entities.WorkSession;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.WorkPeriod;
import com.company.timetracking.domain.valueobjects.WorkSessionId;
import com.company.timetracking.infrastructure.persistence.jpa.entities.WorkSessionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkSessionJpaMapper {

    public WorkSessionJpaEntity toJpa(WorkSession domain) {
        return new WorkSessionJpaEntity(
                domain.id().value(),
                domain.collaboratorId().value(),
                domain.status(),
                domain.startedAt(),
                domain.endedAt(),
                domain.totalMinutes()
        );
    }

    public WorkSession toDomain(WorkSessionJpaEntity jpa) {
        WorkPeriod period = new WorkPeriod(jpa.getStartedAt(), jpa.getEndedAt());
        return WorkSession.restore(
                WorkSessionId.of(jpa.getId()),
                CollaboratorId.of(jpa.getCollaboratorId()),
                jpa.getStatus(),
                period
        );
    }
}
