package com.company.timetracking.domain.repositories;

import com.company.timetracking.domain.entities.Collaborator;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import com.company.timetracking.domain.valueobjects.Email;

import java.util.Optional;

public interface CollaboratorRepository {

    Collaborator save(Collaborator collaborator);

    Optional<Collaborator> findById(CollaboratorId id);

    Optional<Collaborator> findByEmail(Email email);

    boolean existsByEmail(Email email);

    boolean existsById(CollaboratorId id);

    Page<Collaborator> findAll(PageQuery query);

    void deleteById(CollaboratorId id);
}
