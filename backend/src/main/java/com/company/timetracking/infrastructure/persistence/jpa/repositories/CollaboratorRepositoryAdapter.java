package com.company.timetracking.infrastructure.persistence.jpa.repositories;

import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.repositories.Page;
import com.company.timetracking.domain.repositories.PageQuery;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;
import com.company.timetracking.infrastructure.persistence.jpa.entities.CollaboratorJpaEntity;
import com.company.timetracking.infrastructure.persistence.jpa.mappers.CollaboratorJpaMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Outbound adapter implementing the {@link CollaboratorRepository} port over
 * Spring Data JPA. Translates the domain pagination contract to/from Spring's.
 */
@Component
public class CollaboratorRepositoryAdapter implements CollaboratorRepository {

    private final CollaboratorJpaRepository jpa;
    private final CollaboratorJpaMapper mapper;

    public CollaboratorRepositoryAdapter(CollaboratorJpaRepository jpa,
                                         CollaboratorJpaMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Collaborator save(Collaborator collaborator) {
        // Find-or-create keeps lifecycle timestamps intact on update.
        CollaboratorJpaEntity entity = jpa.findById(collaborator.id().value())
                .orElseGet(() -> mapper.toJpa(collaborator));
        entity.setName(collaborator.name());
        entity.setEmail(collaborator.email().value());
        entity.setCargo(collaborator.cargo());
        entity.setStatus(collaborator.status());
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Collaborator> findById(CollaboratorId id) {
        return jpa.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Collaborator> findByEmail(Email email) {
        return jpa.findByEmailIgnoreCase(email.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmailIgnoreCase(email.value());
    }

    @Override
    public boolean existsById(CollaboratorId id) {
        return jpa.existsById(id.value());
    }

    @Override
    public Page<Collaborator> findAll(PageQuery query) {
        var pageable = PageRequest.of(query.page(), query.size(), Sort.by("name").ascending());
        var result = jpa.findAll(pageable);
        return new Page<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }

    @Override
    public void deleteById(CollaboratorId id) {
        jpa.deleteById(id.value());
    }
}
