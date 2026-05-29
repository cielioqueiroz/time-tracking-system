package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Fetches a single collaborator by id. */
@Service
public class GetCollaboratorUseCase {

    private final CollaboratorRepository repository;
    private final CollaboratorDtoMapper mapper;

    public GetCollaboratorUseCase(CollaboratorRepository repository,
                                  CollaboratorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CollaboratorDto execute(String collaboratorId) {
        return repository.findById(CollaboratorId.of(collaboratorId))
                .map(mapper::toDto)
                .orElseThrow(() -> new CollaboratorNotFoundException(collaboratorId));
    }
}
