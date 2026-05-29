package com.company.timetracking.application.mappers;

import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.domain.entities.WorkSession;
import org.springframework.stereotype.Component;

@Component
public class WorkSessionDtoMapper {

    public WorkSessionDto toDto(WorkSession session) {
        return new WorkSessionDto(
                session.id().toString(),
                session.collaboratorId().toString(),
                session.status(),
                session.startedAt(),
                session.endedAt(),
                session.totalMinutes()
        );
    }
}
