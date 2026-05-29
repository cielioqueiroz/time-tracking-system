package com.company.timetracking.application.usecases.collaborator;

import com.company.timetracking.domain.exceptions.CollaboratorNotFoundException;
import com.company.timetracking.domain.repositories.CollaboratorRepository;
import com.company.timetracking.domain.valueobjects.CollaboratorId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Deletes a collaborator (and, by FK cascade, their sessions). */
@Service
public class DeleteCollaboratorUseCase {

    private final CollaboratorRepository repository;

    public DeleteCollaboratorUseCase(CollaboratorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(String collaboratorId) {
        CollaboratorId id = CollaboratorId.of(collaboratorId);
        if (!repository.existsById(id)) {
            throw new CollaboratorNotFoundException(collaboratorId);
        }
        repository.deleteById(id);
    }
}
