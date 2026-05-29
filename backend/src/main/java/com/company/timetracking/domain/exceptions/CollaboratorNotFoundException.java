package com.company.timetracking.domain.exceptions;

public class CollaboratorNotFoundException extends DomainException {
    public CollaboratorNotFoundException(String id) {
        super("COLLABORATOR_NOT_FOUND", "Colaborador não encontrado: " + id);
    }
}
