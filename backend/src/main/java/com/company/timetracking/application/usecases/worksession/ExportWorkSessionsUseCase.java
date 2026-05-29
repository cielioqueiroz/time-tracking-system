package com.company.timetracking.application.usecases.worksession;

import com.company.timetracking.application.dto.WorkSessionDto;
import com.company.timetracking.application.mappers.WorkSessionDtoMapper;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.WorkSessionRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExportWorkSessionsUseCase {

    private final CollaboratorRepository collaboratorRepository;
    private final WorkSessionRepository workSessionRepository;
    private final WorkSessionDtoMapper mapper;

    public ExportWorkSessionsUseCase(CollaboratorRepository collaboratorRepository,
                                     WorkSessionRepository workSessionRepository,
                                     WorkSessionDtoMapper mapper) {
        this.collaboratorRepository = collaboratorRepository;
        this.workSessionRepository = workSessionRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<WorkSessionDto> execute(String collaboratorId) {
        CollaboratorId id = CollaboratorId.of(collaboratorId);
        if (!collaboratorRepository.existsById(id)) {
            throw new CollaboratorNotFoundException(collaboratorId);
        }
        return workSessionRepository.findAllByCollaborator(id).stream()
                .map(mapper::toDto)
                .toList();
    }
}
