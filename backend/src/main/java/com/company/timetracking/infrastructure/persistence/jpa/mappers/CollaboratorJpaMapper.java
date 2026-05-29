package com.company.timetracking.infrastructure.persistence.jpa.mappers;

import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;
import com.company.timetracking.infrastructure.persistence.jpa.entities.CollaboratorJpaEntity;
import org.springframework.stereotype.Component;

/** Translates between the domain {@link Collaborator} and its JPA entity. */
@Component
public class CollaboratorJpaMapper {

    public CollaboratorJpaEntity toJpa(Collaborator domain) {
        return new CollaboratorJpaEntity(
                domain.id().value(),
                domain.name(),
                domain.email().value(),
                domain.cargo(),
                domain.status()
        );
    }

    public Collaborator toDomain(CollaboratorJpaEntity jpa) {
        return Collaborator.restore(
                CollaboratorId.of(jpa.getId()),
                jpa.getName(),
                Email.of(jpa.getEmail()),
                jpa.getCargo(),
                jpa.getStatus()
        );
    }
}
