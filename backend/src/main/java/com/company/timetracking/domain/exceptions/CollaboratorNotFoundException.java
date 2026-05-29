package com.company.timetracking.domain.exceptions;

/** Raised when a collaborator referenced by id does not exist. */
public class CollaboratorNotFoundException extends DomainException {
    public CollaboratorNotFoundException(String id) {
        super("COLLABORATOR_NOT_FOUND", "Colaborador não encontrado: " + id);
    }
}
