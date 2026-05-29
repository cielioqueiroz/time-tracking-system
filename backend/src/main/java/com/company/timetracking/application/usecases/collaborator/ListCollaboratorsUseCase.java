package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.application.dto.PageDto;
import com.company.timetracking.application.mappers.CollaboratorDtoMapper;
import com.company.timetracking.application.queries.ListCollaboratorsQuery;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.PageQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Lists collaborators with pagination. */
@Service
public class ListCollaboratorsUseCase {

    private final CollaboratorRepository repository;
    private final CollaboratorDtoMapper mapper;

    public ListCollaboratorsUseCase(CollaboratorRepository repository,
                                    CollaboratorDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public PageDto<CollaboratorDto> execute(ListCollaboratorsQuery query) {
        var page = repository.findAll(PageQuery.of(query.page(), query.size()))
                .map(mapper::toDto);
        return PageDto.from(page);
    }
}
