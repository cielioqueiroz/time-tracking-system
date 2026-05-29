package com.company.timetracking.application.mappers;

import com.company.timetracking.application.dto.CollaboratorDto;
import com.company.timetracking.domain.entities.Collaborator;
import org.springframework.stereotype.Component;

/** Maps the {@link Collaborator} aggregate to its application read model. */
@Component
public class CollaboratorDtoMapper {

    public CollaboratorDto toDto(Collaborator collaborator) {
        return new CollaboratorDto(
                collaborator.id().toString(),
                collaborator.name(),
                collaborator.email().value(),
                collaborator.cargo(),
                collaborator.status()
        );
    }
}
