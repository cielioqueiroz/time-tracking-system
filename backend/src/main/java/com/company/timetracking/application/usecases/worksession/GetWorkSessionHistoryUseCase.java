package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.dto.PageDto;
import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.application.queries.WorkSessionHistoryQuery;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.PageQuery;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Returns the paginated work-session history of a collaborator (newest first). */
@Service
public class GetWorkSessionHistoryUseCase {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkSessionRepository workSessionRepository;
    private final WorkSessionDtoMapper mapper;

    public GetWorkSessionHistoryUseCase(CollaboratorRepository collaboratorRepository,
                                        WorkSessionRepository workSessionRepository,
                                        WorkSessionDtoMapper mapper) {
        this.collaboratorRepository = collaboratorRepository;
        this.workSessionRepository = workSessionRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageDto<WorkSessionDto> execute(WorkSessionHistoryQuery query) {
        CollaboratorId collaboratorId = CollaboratorId.of(query.collaboratorId());
        if (!collaboratorRepository.existsById(collaboratorId)) {
            throw new CollaboratorNotFoundException(query.collaboratorId());
        }
        var page = workSessionRepository
                .findByCollaborator(collaboratorId, PageQuery.of(query.page(), query.size()))
                .map(mapper::toDto);
        return PageDto.from(page);
    }
}
