package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.dto.WorkSummaryDto;
import com.company.timetracking.application.queries.WorkSummaryQuery;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.repositories.WorkSessionSummary;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetWorkSummaryUseCase {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkSessionRepository workSessionRepository;

    public GetWorkSummaryUseCase(CollaboratorRepository collaboratorRepository,
                                 WorkSessionRepository workSessionRepository) {
        this.collaboratorRepository = collaboratorRepository;
        this.workSessionRepository = workSessionRepository;
    }

    @Transactional(readOnly = true)
    public WorkSummaryDto execute(WorkSummaryQuery query) {
        CollaboratorId collaboratorId = CollaboratorId.of(query.collaboratorId());
        if (!collaboratorRepository.existsById(collaboratorId)) {
            throw new CollaboratorNotFoundException(query.collaboratorId());
        }
        WorkSessionSummary summary = workSessionRepository
                .summaryByCollaborator(collaboratorId, query.from(), query.to());
        return new WorkSummaryDto(
                query.collaboratorId(),
                summary.totalSessions(),
                summary.finishedSessions(),
                summary.totalMinutes());
    }
}
